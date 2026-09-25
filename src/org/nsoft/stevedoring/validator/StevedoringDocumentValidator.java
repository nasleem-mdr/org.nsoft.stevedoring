package org.nsoft.stevedoring.validator;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Validator pelengkap (di luar business logic finance yang sudah ada di
 * SoFFinanceService/DocAction.completeIt()): menyinkronkan status dokumen
 * lain yang tidak semestinya jadi tanggung jawab SoFFinanceService.
 *
 * Saat ini menangani: setelah STEV_StatementOfFact Completed, tandai
 * STEV_VesselSchedule terkait sebagai Completed juga (operasional bongkar
 * muat kapal tersebut sudah selesai total secara administratif).
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

        engine.addDocValidate(MStevStatementOfFact.Table_Name, this);
    }

    @Override
    public String modelChange(PO po, int type) throws Exception
    {
        return null; // tidak digunakan untuk kasus ini
    }

    @Override
    public String docValidate(PO po, int timing)
    {
        if (po instanceof MStevStatementOfFact && timing == TIMING_AFTER_COMPLETE)
        {
            MStevStatementOfFact sof = (MStevStatementOfFact) po;
            MStevVesselSchedule schedule = sof.getVesselSchedule();
            if (!MStevVesselSchedule.DOCSTATUS_Completed.equals(schedule.getDocStatus()))
            {
                schedule.setDocStatus(MStevVesselSchedule.DOCSTATUS_Completed);
                if (!schedule.save())
                    log.warning("Gagal auto-update DocStatus Vessel Schedule "
                        + schedule.getDocumentNo() + " menjadi Completed");
            }
        }
        return null; // null = tidak ada error, lanjutkan
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