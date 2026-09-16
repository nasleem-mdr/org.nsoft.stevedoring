package org.nsoft.stevedoring.validator;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine; // Fix: Added missing import
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Validator pelengkap: menyinkronkan status dokumen lain saat SoF selesai.
 */
public class StevedoringDocumentValidator implements ModelValidator
{
    private static final CLogger log = CLogger.getCLogger(StevedoringDocumentValidator.class);

    private int m_AD_Client_ID = -1;

    @Override
    public void initialize(ModelValidationEngine engine, MClient client)
    {
        if (client != null)
            m_AD_Client_ID = client.getAD_Client_ID();

        // Fix: Gunakan addDocValidate untuk event siklus dokumen (DocAction / Document Engine)
        engine.addDocValidate(MStevStatementOfFact.Table_Name, this);
    }

    @Override
    public String modelChange(PO po, int type) throws Exception
    {
        return null;
    }

    @Override
    public String docValidate(PO po, int timing)
    {
        // Fix: Gunakan TIMING_AFTER_COMPLETE pada docValidate
        if (po instanceof MStevStatementOfFact && timing == TIMING_AFTER_COMPLETE)
        {
            MStevStatementOfFact sof = (MStevStatementOfFact) po;
            MStevVesselSchedule schedule = sof.getVesselSchedule();
            
            if (schedule != null && !MStevVesselSchedule.DOCSTATUS_Completed.equals(schedule.getDocStatus()))
            {
                schedule.setDocStatus(MStevVesselSchedule.DOCSTATUS_Completed);
                if (!schedule.save(po.get_TrxName())) // Pastikan disimpan dalam transaksi yang sama
                    log.warning("Gagal auto-update DocStatus Vessel Schedule "
                            + schedule.getDocumentNo() + " menjadi Completed");
            }
        }
        return null; // null = sukses / lanjutkan
    }

    @Override
    public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
    {
        return null;
    }

    @Override
    public int getAD_Client_ID()
    {
        return m_AD_Client_ID;
    }
}