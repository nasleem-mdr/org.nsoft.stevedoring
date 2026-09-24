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

/** Generated Model for STEV_TallySheet
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_TallySheet")
public class X_STEV_TallySheet extends PO implements I_STEV_TallySheet, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260917L;

    /** Standard Constructor */
    public X_STEV_TallySheet (Properties ctx, int STEV_TallySheet_ID, String trxName)
    {
      super (ctx, STEV_TallySheet_ID, trxName);
      /** if (STEV_TallySheet_ID == 0)
        {
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setSTEV_TallySheet_ID (0);
			setSTEV_VesselSchedule_ID (0);
			setShiftNo (null);
			setTallyDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Standard Constructor */
    public X_STEV_TallySheet (Properties ctx, int STEV_TallySheet_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_TallySheet_ID, trxName, virtualColumns);
      /** if (STEV_TallySheet_ID == 0)
        {
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setSTEV_TallySheet_ID (0);
			setSTEV_VesselSchedule_ID (0);
			setShiftNo (null);
			setTallyDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Standard Constructor */
    public X_STEV_TallySheet (Properties ctx, String STEV_TallySheet_UU, String trxName)
    {
      super (ctx, STEV_TallySheet_UU, trxName);
      /** if (STEV_TallySheet_UU == null)
        {
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setSTEV_TallySheet_ID (0);
			setSTEV_VesselSchedule_ID (0);
			setShiftNo (null);
			setTallyDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Standard Constructor */
    public X_STEV_TallySheet (Properties ctx, String STEV_TallySheet_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_TallySheet_UU, trxName, virtualColumns);
      /** if (STEV_TallySheet_UU == null)
        {
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setSTEV_TallySheet_ID (0);
			setSTEV_VesselSchedule_ID (0);
			setShiftNo (null);
			setTallyDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_STEV_TallySheet (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_TallySheet[")
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

	/** Set STEV_TallySheet.
		@param STEV_TallySheet_ID STEV_TallySheet
	*/
	public void setSTEV_TallySheet_ID (int STEV_TallySheet_ID)
	{
		if (STEV_TallySheet_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_TallySheet_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_TallySheet_ID, Integer.valueOf(STEV_TallySheet_ID));
	}

	/** Get STEV_TallySheet.
		@return STEV_TallySheet	  */
	public int getSTEV_TallySheet_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_TallySheet_ID);
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

	/** Shift 1 = Shift1 */
	public static final String SHIFTNO_Shift1 = "Shift1";
	/** Shift 2 = Shift2 */
	public static final String SHIFTNO_Shift2 = "Shift2";
	/** Shift 3 = Shift3 */
	public static final String SHIFTNO_Shift3 = "Shift3";
	/** Set ShiftNo.
		@param ShiftNo ShiftNo
	*/
	public void setShiftNo (String ShiftNo)
	{

		set_Value (COLUMNNAME_ShiftNo, ShiftNo);
	}

	/** Get ShiftNo.
		@return ShiftNo	  */
	public String getShiftNo()
	{
		return (String)get_Value(COLUMNNAME_ShiftNo);
	}

	/** Set TallyDate.
		@param TallyDate TallyDate
	*/
	public void setTallyDate (Timestamp TallyDate)
	{
		set_Value (COLUMNNAME_TallyDate, TallyDate);
	}

	/** Get TallyDate.
		@return TallyDate	  */
	public Timestamp getTallyDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_TallyDate);
	}
}