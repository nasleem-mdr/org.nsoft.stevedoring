/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - Nasleem - NSoft - IDempiere                                       *
 **********************************************************************/

package org.nsoft.stevedoring.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.nsoft.stevedoring.service.SoFFinanceService;

/**
 * Statement of Fact (SoF) — The final aggregation of all {@link MStevTallyLine} 
 * records under a single {@link MStevVesselSchedule}.
 * <p>
 * Implements {@link DocAction} following standard iDempiere document patterns 
 * (similar to MOrder / MInOut):
 * <ul>
 *   <li>Standard document controls (Complete, Void, Close) are automatically available on the toolbar.</li>
 *   <li>Upon completion, {@link #completeIt()} delegates to 
 *       {@link SoFFinanceService#processCompletion(MStevStatementOfFact)} 
 *       to execute Auto-Adjustment & Delivery (updates QtyOrdered on C_OrderLine per product, 
 *       then generates and completes M_InOut).</li>
 *   <li>C_Invoice creation is deliberately decoupled to accommodate flexible invoicing schedules 
 *       (e.g., batched or monthly billing) via standard iDempiere invoicing processes.</li>
 * </ul>
 * </p>
 * <b>Configuration Note:</b> {@code AD_Table.ClassName} for {@code STEV_StatementOfFact} 
 * MUST be set to this class: {@code org.nsoft.stevedoring.model.MStevStatementOfFact}.
 */
public class MStevStatementOfFact extends X_STEV_StatementOfFact implements DocAction
{
    private static final long serialVersionUID = 1L;

    private static final CLogger slog = CLogger.getCLogger(MStevStatementOfFact.class);

    private String m_processMsg = null;

    public static final String DOCSTATUS_Draft      = "DR";
    public static final String DOCSTATUS_InProgress = "IP";
    public static final String DOCSTATUS_Completed  = "CO";
    public static final String DOCSTATUS_Voided     = "VO";
    public static final String DOCSTATUS_Closed     = "CL";

    public MStevStatementOfFact(Properties ctx, int STEV_StatementOfFact_ID, String trxName)
    {
        super(ctx, STEV_StatementOfFact_ID, trxName);
    }

    public MStevStatementOfFact(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    public MStevVesselSchedule getVesselSchedule()
    {
        return new MStevVesselSchedule(getCtx(), getSTEV_VesselSchedule_ID(), get_TrxName());
    }

    public List<MStevStatementOfFactLine> getLines()
    {
        return new Query(getCtx(), MStevStatementOfFactLine.Table_Name,
                COLUMNNAME_STEV_StatementOfFact_ID + "=?", get_TrxName())
                .setParameters(getSTEV_StatementOfFact_ID())
                .setOrderBy(MStevStatementOfFactLine.COLUMNNAME_Line)
                .list();
    }

    public List<MStevTallyLine> getSourceTallyLines()
    {
        String whereClause = "STEV_TallySheet_ID IN (SELECT STEV_TallySheet_ID FROM STEV_TallySheet "
                + "WHERE STEV_VesselSchedule_ID=?)";
        return new Query(getCtx(), MStevTallyLine.Table_Name, whereClause, get_TrxName())
                .setParameters(getSTEV_VesselSchedule_ID())
                .setOrderBy(MStevTallyLine.COLUMNNAME_Line)
                .list();
    }

    /**
     * Regenerates STEV_StatementOfFactLine by aggregating the latest STEV_TallyLine data, 
     * grouped by (M_Product_ID, C_UOM_ID) to support mixed UOMs.
     *
     * Called during:
     * - afterSave() (Draft/In Progress): Allows pre-completion breakdown review.
     * - prepareIt(): Locks the FINAL snapshot for SoFFinanceService before completion.
     * No-op once DocStatus is Completed (data is frozen).
     *
     * IMPORTANT:
     * 1. Must run in afterSave() (not beforeSave()) so child FKs can reference a persisted header.
     * 2. Header TotalQtyRealized is updated via direct SQL to avoid unnecessary model save recursion.
     */
    public void regenerateLines()
    {
        if (DOCSTATUS_Completed.equals(getDocStatus()))
            return; 
        
        if (getSTEV_StatementOfFact_ID() <= 0)
        {
            slog.warning("regenerateLines() dipanggil sebelum header SoF ter-simpan (ID<=0) — dibatalkan");
            return;
        }
        if (getSTEV_VesselSchedule_ID() <= 0)
        {
            slog.warning("SoF " + getDocumentNo() + ": STEV_VesselSchedule_ID is not set (0) — "
            		+ "regenerateLines() is canceled, StatementOfFactLine will not be populated until "
            		+ "Vessel Schedule is selected and then SoF is resaved");
            return;
        }
        
        List<MStevStatementOfFactLine> oldLines = getLines();
        for (MStevStatementOfFactLine old : oldLines)
            old.deleteEx(true);
        if (!oldLines.isEmpty())
            slog.fine("SoF " + getDocumentNo() + ": " + oldLines.size() + " old rows are deleted before regenerate");

        MOrder order = getVesselSchedule().getOrder();
        if (order == null || order.getC_Order_ID() <= 0)
        {
            slog.warning("SoF " + getDocumentNo() + ": VesselSchedule (ID="
                    + getSTEV_VesselSchedule_ID() + ") No related C_Order — "
                    + "regenerateLines() canceled");
            return;
        }

        java.util.Map<String, MOrderLine> orderLineByProductUOM = new java.util.LinkedHashMap<>();
        java.util.Map<Integer, java.util.List<MOrderLine>> orderLinesByProduct = new java.util.LinkedHashMap<>();
        for (MOrderLine ol : order.getLines(true, null))
        {
            orderLineByProductUOM.put(ol.getM_Product_ID() + "_" + ol.getC_UOM_ID(), ol);
            orderLinesByProduct
                    .computeIfAbsent(ol.getM_Product_ID(), k -> new java.util.ArrayList<>())
                    .add(ol);
        }

        String sql = "SELECT tl.M_Product_ID, tl.C_UOM_ID, COALESCE(SUM(tl.QtyMoved),0) "
                + "FROM STEV_TallyLine tl "
                + "JOIN STEV_TallySheet ts ON ts.STEV_TallySheet_ID = tl.STEV_TallySheet_ID "
                + "WHERE ts.STEV_VesselSchedule_ID = ? AND tl.IsActive='Y' AND ts.IsActive='Y' "
                + "GROUP BY tl.M_Product_ID, tl.C_UOM_ID "
                + "ORDER BY tl.M_Product_ID";

        BigDecimal total = BigDecimal.ZERO;
        int lineNo = 10;
        int rowCount = 0;
        try (java.sql.PreparedStatement pstmt = org.compiere.util.DB.prepareStatement(sql, get_TrxName()))
        {
            pstmt.setInt(1, getSTEV_VesselSchedule_ID());
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    rowCount++;
                    int productId = rs.getInt(1);
                    int uomId = rs.getInt(2);
                    BigDecimal qty = rs.getBigDecimal(3);

                    if (qty == null || qty.signum() <= 0)
                    {
                        slog.warning("SoF " + getDocumentNo() + ": M_Product_ID=" + productId
                                + " C_UOM_ID=" + uomId + " have QtyMoved total <= 0 — skipped lines");
                        continue;
                    }

                    MStevStatementOfFactLine line = new MStevStatementOfFactLine(getCtx(), 0, get_TrxName());
                    line.setSTEV_StatementOfFact_ID(getSTEV_StatementOfFact_ID());
                    line.setLine(lineNo);
                    line.setM_Product_ID(productId);
                    line.setC_UOM_ID(uomId);
                    line.setQtyRealized(qty);

                    MOrderLine matchingOrderLine = orderLineByProductUOM.get(productId + "_" + uomId);
                    if (matchingOrderLine == null)
                    {
                        java.util.List<MOrderLine> candidates = orderLinesByProduct.get(productId);
                        if (candidates != null && candidates.size() == 1)
                        {                            
                            matchingOrderLine = candidates.get(0);
                            slog.warning("SoF " + getDocumentNo() + ": M_Product_ID=" + productId
                                    + " match to C_OrderLine via fallback (UOM Sales Order different from UOM Tally)");
                        }
                    }
                    if (matchingOrderLine != null)
                        line.setC_OrderLine_ID(matchingOrderLine.getC_OrderLine_ID());
                    else
                        slog.warning("SoF " + getDocumentNo() + ": M_Product_ID=" + productId
                                + " C_UOM_ID=" + uomId + " not found C_OrderLine related in Sales Order "
                                + order.getDocumentNo());
                    
                    line.saveEx();

                    total = total.add(qty);
                    lineNo += 10;
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("regenerate failed STEV_StatementOfFactLine for SoF "
                    + getDocumentNo(), e);
        }

        slog.info("SoF " + getDocumentNo() + " (VesselSchedule_ID=" + getSTEV_VesselSchedule_ID()
                + "): " + rowCount + " grup TallyLine not found, realization total=" + total);
        if (rowCount == 0)
        {
        	slog.warning("SoF " + getDocumentNo() + ": 0 active STEV_TallyLine rows found for "
        			+ "VesselSchedule_ID=" + getSTEV_VesselSchedule_ID() + " — make sure there is a "
        			+ "STEV_TallySheet (IsActive=Y) that points to this VesselSchedule and already has a STEV_TallyLine (IsActive=Y) in it");
        }

        // Update header langsung via SQL — hindari rekursi save()
        org.compiere.util.DB.executeUpdateEx(
                "UPDATE STEV_StatementOfFact SET TotalQtyRealized=? WHERE STEV_StatementOfFact_ID=?",
                new Object[] { total, getSTEV_StatementOfFact_ID() }, get_TrxName());
        setTotalQtyRealized(total); 
    }

    @Override
    protected boolean afterSave(boolean newRecord, boolean success)
    {
        if (!success)
            return success;

        if (DOCSTATUS_Draft.equals(getDocStatus()) || DOCSTATUS_InProgress.equals(getDocStatus()))
            regenerateLines();

        return true;
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (DOCSTATUS_Completed.equals(getDocStatus()) && getDigitalSignature() == null)
        {
            log.saveError("Error", "The Master of Ship's digital signature must be filled in before Complete");
            return false;
        }
        return true;
    }

    // =====================================================================
    // Implementasi org.compiere.process.DocAction
    // =====================================================================

    @Override
    public boolean processIt(String processAction) throws Exception
    {
        return new DocumentEngine(this, getDocStatus()).processIt(processAction, getDocAction());
    }

    @Override
    public boolean unlockIt()
    {
        setProcessing(false);
        return true;
    }

    @Override
    public boolean invalidateIt()
    {
        setDocAction(DOCACTION_Complete);
        return true;
    }

    @Override
    public String prepareIt()
    {
        
        regenerateLines();

        if (getTotalQtyRealized() == null || getTotalQtyRealized().signum() <= 0)
        {
            m_processMsg = "Tidak ada realisasi tonase (TallyLine kosong) — tidak bisa Complete";
            return DocAction.STATUS_Invalid;
        }
        if (getDigitalSignature() == null || getDigitalSignature().isEmpty())
        {
            m_processMsg = "Tanda tangan digital Master Kapal wajib diisi";
            return DocAction.STATUS_Invalid;
        }
        setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }

    
    /**
    * Core process: Auto-Adjustment & Delivery. Full delegation to
    * SoFFinanceService so that finance/inventory logic is centralized, easily
    * tested, and avoids bloating model classes.
    */
    @Override
    public String completeIt()
    {
        String status = prepareIt();
        if (!DocAction.STATUS_InProgress.equals(status))
            return status;

        try
        {
            SoFFinanceService service = new SoFFinanceService();
            service.processCompletion(this);
            setProcessed(true);
            setDocAction(DOCACTION_Close);
            setDocStatus(DOCSTATUS_Completed);
            m_processMsg = "Statement of Fact is processed — QtyOrdered is updated and M_InOut is Complete";
            return DocAction.STATUS_Completed;
        }
        catch (Exception e)
        {
            slog.severe("Failed to process SoF completion: " + e.getMessage());
            m_processMsg = "Error during Auto-Adjustment & Delivery: " + e.getMessage();
            return DocAction.STATUS_Invalid;
        }
    }

    @Override
    public boolean voidIt()
    {
        if (DOCSTATUS_Completed.equals(getDocStatus()))
        {
        	m_processMsg = "A Completed SoF (which has triggered M_InOut, possibly invoiced) cannot be Voided directly — "
        			+ "use Reverse Correct or void the child document first";
            return false;
        }
        setDocStatus(DOCSTATUS_Voided);
        setDocAction(DOCACTION_None);
        return true;
    }

    @Override
    public boolean closeIt()
    {
        setDocStatus(DOCSTATUS_Closed);
        setDocAction(DOCACTION_None);
        return true;
    }

    @Override
    public boolean reverseCorrectIt()
    {
    	m_processMsg = "Reverse the child documents (M_InOut/C_Invoice) first before reversing this SoF";
        return false;
    }

    @Override
    public boolean reverseAccrualIt()
    {
        return false;
    }

    @Override
    public boolean reActivateIt()
    {
        setDocStatus(DOCSTATUS_InProgress);
        setDocAction(DOCACTION_Complete);
        return true;
    }

    @Override
    public String getSummary()
    {
        StringBuilder sb = new StringBuilder(getDocumentNo());
        if (m_processMsg != null)
            sb.append(" - ").append(m_processMsg);
        return sb.toString();
    }

    @Override
    public String getDocumentInfo()
    {
        return "Statement of Fact " + getDocumentNo();
    }

    @Override
    public java.io.File createPDF()
    {
        return null; 
    }

    @Override
    public String getProcessMsg()
    {
        return m_processMsg;
    }

    @Override
    public int getC_Currency_ID()
    {
        return -1; 
    }
    @Override
    public BigDecimal getApprovalAmt()
    {
        return Env.ZERO; 
    }
    @Override
    public int getDoc_User_ID()
    {
        return getCreatedBy();
    }

    @Override
    public boolean approveIt()
    {
        setIsApproved(true);
        return true;
    }

    @Override
    public boolean rejectIt()
    {
        setIsApproved(false);
        return true;
    }

    @Override
    public boolean isApproved()
    {
        return true; 
    }

    @Override
    public String toString()
    {
        return "MStevStatementOfFact[" + getSTEV_StatementOfFact_ID() + ", DocumentNo=" + getDocumentNo() + "]";
    }
}