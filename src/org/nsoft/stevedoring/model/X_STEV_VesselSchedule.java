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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for STEV_VesselSchedule
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_VesselSchedule")
public class X_STEV_VesselSchedule extends PO implements I_STEV_VesselSchedule, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260916L;

    /** Standard Constructor */
    public X_STEV_VesselSchedule (Properties ctx, int STEV_VesselSchedule_ID, String trxName)
    {
      super (ctx, STEV_VesselSchedule_ID, trxName);
      /** if (STEV_VesselSchedule_ID == 0)
        {
			setActivityType (null);
			setC_BPartner_ID (0);
			setC_Order_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
// N
			setSTEV_VesselSchedule_ID (0);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_VesselSchedule (Properties ctx, int STEV_VesselSchedule_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_VesselSchedule_ID, trxName, virtualColumns);
      /** if (STEV_VesselSchedule_ID == 0)
        {
			setActivityType (null);
			setC_BPartner_ID (0);
			setC_Order_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
// N
			setSTEV_VesselSchedule_ID (0);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_VesselSchedule (Properties ctx, String STEV_VesselSchedule_UU, String trxName)
    {
      super (ctx, STEV_VesselSchedule_UU, trxName);
      /** if (STEV_VesselSchedule_UU == null)
        {
			setActivityType (null);
			setC_BPartner_ID (0);
			setC_Order_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
// N
			setSTEV_VesselSchedule_ID (0);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_VesselSchedule (Properties ctx, String STEV_VesselSchedule_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_VesselSchedule_UU, trxName, virtualColumns);
      /** if (STEV_VesselSchedule_UU == null)
        {
			setActivityType (null);
			setC_BPartner_ID (0);
			setC_Order_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
// N
			setSTEV_VesselSchedule_ID (0);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Load Constructor */
    public X_STEV_VesselSchedule (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_VesselSchedule[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ATA.
		@param ATA Actual Time of Arrival
	*/
	public void setATA (Timestamp ATA)
	{
		set_Value (COLUMNNAME_ATA, ATA);
	}

	/** Get ATA.
		@return Actual Time of Arrival
	  */
	public Timestamp getATA()
	{
		return (Timestamp)get_Value(COLUMNNAME_ATA);
	}

	/** Set ATB.
		@param ATB Actual Time of Berthing
	*/
	public void setATB (Timestamp ATB)
	{
		set_Value (COLUMNNAME_ATB, ATB);
	}

	/** Get ATB.
		@return Actual Time of Berthing
	  */
	public Timestamp getATB()
	{
		return (Timestamp)get_Value(COLUMNNAME_ATB);
	}

	/** Set ATD.
		@param ATD Actual Time of Departure
	*/
	public void setATD (Timestamp ATD)
	{
		set_Value (COLUMNNAME_ATD, ATD);
	}

	/** Get ATD.
		@return Actual Time of Departure
	  */
	public Timestamp getATD()
	{
		return (Timestamp)get_Value(COLUMNNAME_ATD);
	}

	/** Discharge / Unloading = Discharge */
	public static final String ACTIVITYTYPE_DischargeUnloading = "Discharge";
	/** Loading = Loading */
	public static final String ACTIVITYTYPE_Loading = "Loading";
	/** Set ActivityType.
		@param ActivityType ActivityType
	*/
	public void setActivityType (String ActivityType)
	{

		set_Value (COLUMNNAME_ActivityType, ActivityType);
	}

	/** Get ActivityType.
		@return ActivityType	  */
	public String getActivityType()
	{
		return (String)get_Value(COLUMNNAME_ActivityType);
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner.
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
	{
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_ID)
			.getPO(getC_Order_ID(), get_TrxName());
	}

	/** Set Order.
		@param C_Order_ID Order
	*/
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set ETA.
		@param ETA Estimated Time of Arrival
	*/
	public void setETA (Timestamp ETA)
	{
		set_Value (COLUMNNAME_ETA, ETA);
	}

	/** Get ETA.
		@return Estimated Time of Arrival
	  */
	public Timestamp getETA()
	{
		return (Timestamp)get_Value(COLUMNNAME_ETA);
	}

	/** Set ETB.
		@param ETB Estimated Time of Berthing
	*/
	public void setETB (Timestamp ETB)
	{
		set_Value (COLUMNNAME_ETB, ETB);
	}

	/** Get ETB.
		@return Estimated Time of Berthing
	  */
	public Timestamp getETB()
	{
		return (Timestamp)get_Value(COLUMNNAME_ETB);
	}

	/** Set ETD.
		@param ETD Estimated Time of Departure 
	*/
	public void setETD (Timestamp ETD)
	{
		set_Value (COLUMNNAME_ETD, ETD);
	}

	/** Get ETD.
		@return Estimated Time of Departure 
	  */
	public Timestamp getETD()
	{
		return (Timestamp)get_Value(COLUMNNAME_ETD);
	}

	/** Set InaportnetPKKNo.
		@param InaportnetPKKNo Nomor PKK (Pemberitahuan Kedatangan Kapal)
	*/
	public void setInaportnetPKKNo (String InaportnetPKKNo)
	{
		set_Value (COLUMNNAME_InaportnetPKKNo, InaportnetPKKNo);
	}

	/** Get InaportnetPKKNo.
		@return Nomor PKK (Pemberitahuan Kedatangan Kapal)
	  */
	public String getInaportnetPKKNo()
	{
		return (String)get_Value(COLUMNNAME_InaportnetPKKNo);
	}

	/** Set InaportnetPPKBNo.
		@param InaportnetPPKBNo Nomor PPKB (Permohonan Pelayanan Kapal &amp; Barang)
	*/
	public void setInaportnetPPKBNo (String InaportnetPPKBNo)
	{
		set_Value (COLUMNNAME_InaportnetPPKBNo, InaportnetPPKBNo);
	}

	/** Get InaportnetPPKBNo.
		@return Nomor PPKB (Permohonan Pelayanan Kapal &amp; Barang)
	  */
	public String getInaportnetPPKBNo()
	{
		return (String)get_Value(COLUMNNAME_InaportnetPPKBNo);
	}

	/** Set InaportnetRKBMNo.
		@param InaportnetRKBMNo Nomor RKBM (Rencana Kegiatan Bongkar Muat)
	*/
	public void setInaportnetRKBMNo (String InaportnetRKBMNo)
	{
		set_Value (COLUMNNAME_InaportnetRKBMNo, InaportnetRKBMNo);
	}

	/** Get InaportnetRKBMNo.
		@return Nomor RKBM (Rencana Kegiatan Bongkar Muat)
	  */
	public String getInaportnetRKBMNo()
	{
		return (String)get_Value(COLUMNNAME_InaportnetRKBMNo);
	}

	/** Set InaportnetStatus.
		@param InaportnetStatus Status approval dari sisi Inaportnet (terpisah dari DocStatus internal)
	*/
	public void setInaportnetStatus (String InaportnetStatus)
	{
		set_Value (COLUMNNAME_InaportnetStatus, InaportnetStatus);
	}

	/** Get InaportnetStatus.
		@return Status approval dari sisi Inaportnet (terpisah dari DocStatus internal)
	  */
	public String getInaportnetStatus()
	{
		return (String)get_Value(COLUMNNAME_InaportnetStatus);
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

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_Berth getSTEV_Berth() throws RuntimeException
	{
		return (I_STEV_Berth)MTable.get(getCtx(), I_STEV_Berth.Table_ID)
			.getPO(getSTEV_Berth_ID(), get_TrxName());
	}

	/** Set STEV_Berth.
		@param STEV_Berth_ID STEV_Berth
	*/
	public void setSTEV_Berth_ID (int STEV_Berth_ID)
	{
		if (STEV_Berth_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_Berth_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_Berth_ID, Integer.valueOf(STEV_Berth_ID));
	}

	/** Get STEV_Berth.
		@return STEV_Berth	  */
	public int getSTEV_Berth_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_Berth_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set STEV_VesselSchedule.
		@param STEV_VesselSchedule_ID STEV_VesselSchedule
	*/
	public void setSTEV_VesselSchedule_ID (int STEV_VesselSchedule_ID)
	{
		if (STEV_VesselSchedule_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_VesselSchedule_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_VesselSchedule_ID, Integer.valueOf(STEV_VesselSchedule_ID));
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

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_Vessel getSTEV_Vessel() throws RuntimeException
	{
		return (I_STEV_Vessel)MTable.get(getCtx(), I_STEV_Vessel.Table_ID)
			.getPO(getSTEV_Vessel_ID(), get_TrxName());
	}

	/** Set STEV_Vessel.
		@param STEV_Vessel_ID STEV_Vessel
	*/
	public void setSTEV_Vessel_ID (int STEV_Vessel_ID)
	{
		if (STEV_Vessel_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_Vessel_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_Vessel_ID, Integer.valueOf(STEV_Vessel_ID));
	}

	/** Get STEV_Vessel.
		@return STEV_Vessel	  */
	public int getSTEV_Vessel_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_Vessel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}