/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.nsoft.stevedoring.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for STEV_StatementOfFact
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_StatementOfFact")
public class X_STEV_StatementOfFact extends PO implements I_STEV_StatementOfFact, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260918L;

    /** Standard Constructor */
    public X_STEV_StatementOfFact (Properties ctx, int STEV_StatementOfFact_ID, String trxName)
    {
      super (ctx, STEV_StatementOfFact_ID, trxName);
      /** if (STEV_StatementOfFact_ID == 0)
        {
			setCompleteButton (null);
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setProcessed (false);
// N
			setSTEV_StatementOfFact_ID (0);
			setSTEV_VesselSchedule_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_StatementOfFact (Properties ctx, int STEV_StatementOfFact_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_StatementOfFact_ID, trxName, virtualColumns);
      /** if (STEV_StatementOfFact_ID == 0)
        {
			setCompleteButton (null);
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setProcessed (false);
// N
			setSTEV_StatementOfFact_ID (0);
			setSTEV_VesselSchedule_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_StatementOfFact (Properties ctx, String STEV_StatementOfFact_UU, String trxName)
    {
      super (ctx, STEV_StatementOfFact_UU, trxName);
      /** if (STEV_StatementOfFact_UU == null)
        {
			setCompleteButton (null);
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setProcessed (false);
// N
			setSTEV_StatementOfFact_ID (0);
			setSTEV_VesselSchedule_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_StatementOfFact (Properties ctx, String STEV_StatementOfFact_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_StatementOfFact_UU, trxName, virtualColumns);
      /** if (STEV_StatementOfFact_UU == null)
        {
			setCompleteButton (null);
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setProcessed (false);
// N
			setSTEV_StatementOfFact_ID (0);
			setSTEV_VesselSchedule_ID (0);
        } */
    }

    /** Load Constructor */
    public X_STEV_StatementOfFact (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_STEV_StatementOfFact[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocType_ID(), get_TrxName());
	}

	/** Set Document Type.
		@param C_DocType_ID Document type or rules
	*/
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0)
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException
	{
		return (org.compiere.model.I_C_UOM)MTable.get(getCtx(), org.compiere.model.I_C_UOM.Table_ID)
			.getPO(getC_UOM_ID(), get_TrxName());
	}

	/** Set UOM.
		@param C_UOM_ID Unit of Measure
	*/
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1)
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CompleteButton.
		@param CompleteButton CompleteButton
	*/
	public void setCompleteButton (String CompleteButton)
	{
		set_Value (COLUMNNAME_CompleteButton, CompleteButton);
	}

	/** Get CompleteButton.
		@return CompleteButton	  */
	public String getCompleteButton()
	{
		return (String)get_Value(COLUMNNAME_CompleteButton);
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set DigitalSignature.
		@param DigitalSignature DigitalSignature
	*/
	public void setDigitalSignature (String DigitalSignature)
	{
		set_Value (COLUMNNAME_DigitalSignature, DigitalSignature);
	}

	/** Get DigitalSignature.
		@return DigitalSignature	  */
	public String getDigitalSignature()
	{
		return (String)get_Value(COLUMNNAME_DigitalSignature);
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** &lt;None&gt; = -- */
	public static final String DOCACTION_None = "--";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Set Document Action.
		@param DocAction The targeted status of the document
	*/
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction()
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Set Document Status.
		@param DocStatus The current status of the document
	*/
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus()
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo Document sequence number of the document
	*/
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set IdleTimeReason.
		@param IdleTimeReason IdleTimeReason
	*/
	public void setIdleTimeReason (String IdleTimeReason)
	{
		set_Value (COLUMNNAME_IdleTimeReason, IdleTimeReason);
	}

	/** Get IdleTimeReason.
		@return IdleTimeReason	  */
	public String getIdleTimeReason()
	{
		return (String)get_Value(COLUMNNAME_IdleTimeReason);
	}

	/** Set Approved.
		@param IsApproved Indicates if this document requires approval
	*/
	public void setIsApproved (boolean IsApproved)
	{
		set_ValueNoCheck (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved()
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set MasterName.
		@param MasterName MasterName
	*/
	public void setMasterName (String MasterName)
	{
		set_Value (COLUMNNAME_MasterName, MasterName);
	}

	/** Get MasterName.
		@return MasterName	  */
	public String getMasterName()
	{
		return (String)get_Value(COLUMNNAME_MasterName);
	}

	/** Set Processed.
		@param Processed The document has been processed
	*/
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed()
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now
	*/
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing()
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set STEV_StatementOfFact.
		@param STEV_StatementOfFact_ID STEV_StatementOfFact
	*/
	public void setSTEV_StatementOfFact_ID (int STEV_StatementOfFact_ID)
	{
		if (STEV_StatementOfFact_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_StatementOfFact_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_StatementOfFact_ID, Integer.valueOf(STEV_StatementOfFact_ID));
	}

	/** Get STEV_StatementOfFact.
		@return STEV_StatementOfFact	  */
	public int getSTEV_StatementOfFact_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_StatementOfFact_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_VesselSchedule getSTEV_VesselSchedule() throws RuntimeException
	{
		return (I_STEV_VesselSchedule)MTable.get(getCtx(), I_STEV_VesselSchedule.Table_ID)
			.getPO(getSTEV_VesselSchedule_ID(), get_TrxName());
	}

	/** Set STEV_VesselSchedule.
		@param STEV_VesselSchedule_ID STEV_VesselSchedule
	*/
	public void setSTEV_VesselSchedule_ID (int STEV_VesselSchedule_ID)
	{
		if (STEV_VesselSchedule_ID < 1)
			set_Value (COLUMNNAME_STEV_VesselSchedule_ID, null);
		else
			set_Value (COLUMNNAME_STEV_VesselSchedule_ID, Integer.valueOf(STEV_VesselSchedule_ID));
	}

	/** Get STEV_VesselSchedule.
		@return STEV_VesselSchedule	  */
	public int getSTEV_VesselSchedule_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_VesselSchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SignedDate.
		@param SignedDate SignedDate
	*/
	public void setSignedDate (Timestamp SignedDate)
	{
		set_Value (COLUMNNAME_SignedDate, SignedDate);
	}

	/** Get SignedDate.
		@return SignedDate	  */
	public Timestamp getSignedDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_SignedDate);
	}

	/** Set TotalIdleTimeMinutes.
		@param TotalIdleTimeMinutes TotalIdleTimeMinutes
	*/
	public void setTotalIdleTimeMinutes (int TotalIdleTimeMinutes)
	{
		set_Value (COLUMNNAME_TotalIdleTimeMinutes, Integer.valueOf(TotalIdleTimeMinutes));
	}

	/** Get TotalIdleTimeMinutes.
		@return TotalIdleTimeMinutes	  */
	public int getTotalIdleTimeMinutes()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TotalIdleTimeMinutes);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TotalQtyRealized.
		@param TotalQtyRealized TotalQtyRealized
	*/
	public void setTotalQtyRealized (BigDecimal TotalQtyRealized)
	{
		set_Value (COLUMNNAME_TotalQtyRealized, TotalQtyRealized);
	}

	/** Get TotalQtyRealized.
		@return TotalQtyRealized	  */
	public BigDecimal getTotalQtyRealized()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalQtyRealized);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}