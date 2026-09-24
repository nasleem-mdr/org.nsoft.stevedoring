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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for STEV_Vessel
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_Vessel")
public class X_STEV_Vessel extends PO implements I_STEV_Vessel, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260922L;

    /** Standard Constructor */
    public X_STEV_Vessel (Properties ctx, int STEV_Vessel_ID, String trxName)
    {
      super (ctx, STEV_Vessel_ID, trxName);
      /** if (STEV_Vessel_ID == 0)
        {
			setC_Country_ID (0);
			setName (null);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_Vessel (Properties ctx, int STEV_Vessel_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_Vessel_ID, trxName, virtualColumns);
      /** if (STEV_Vessel_ID == 0)
        {
			setC_Country_ID (0);
			setName (null);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_Vessel (Properties ctx, String STEV_Vessel_UU, String trxName)
    {
      super (ctx, STEV_Vessel_UU, trxName);
      /** if (STEV_Vessel_UU == null)
        {
			setC_Country_ID (0);
			setName (null);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_Vessel (Properties ctx, String STEV_Vessel_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_Vessel_UU, trxName, virtualColumns);
      /** if (STEV_Vessel_UU == null)
        {
			setC_Country_ID (0);
			setName (null);
			setSTEV_Vessel_ID (0);
        } */
    }

    /** Load Constructor */
    public X_STEV_Vessel (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_Vessel[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Beam.
		@param Beam The overall width of a vessel at its widest point, measured from the outermost edge of the hull on one side to the outermost edge on the other
	*/
	public void setBeam (BigDecimal Beam)
	{
		set_Value (COLUMNNAME_Beam, Beam);
	}

	/** Get Beam.
		@return The overall width of a vessel at its widest point, measured from the outermost edge of the hull on one side to the outermost edge on the other
	  */
	public BigDecimal getBeam()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Beam);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_Country getC_Country() throws RuntimeException
	{
		return (org.compiere.model.I_C_Country)MTable.get(getCtx(), org.compiere.model.I_C_Country.Table_ID)
			.getPO(getC_Country_ID(), get_TrxName());
	}

	/** Set Flag Country.
		@param C_Country_ID Flag Country 
	*/
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Country_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
	}

	/** Get Flag Country.
		@return Flag Country 
	  */
	public int getC_Country_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Call Sign.
		@param CallSign A unique alphanumeric identifier assigned to a ship by its flag state&#039;s telecommunications authority.
	*/
	public void setCallSign (String CallSign)
	{
		set_Value (COLUMNNAME_CallSign, CallSign);
	}

	/** Get Call Sign.
		@return A unique alphanumeric identifier assigned to a ship by its flag state&#039;s telecommunications authority.
	  */
	public String getCallSign()
	{
		return (String)get_Value(COLUMNNAME_CallSign);
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

	/** Set DraftMeter.
		@param DraftMeter Measures the vertical distance between a vessel&#8217;s keel (the absolute bottom of the ship) and its waterline
	*/
	public void setDraftMeter (BigDecimal DraftMeter)
	{
		set_Value (COLUMNNAME_DraftMeter, DraftMeter);
	}

	/** Get DraftMeter.
		@return Measures the vertical distance between a vessel&#8217;s keel (the absolute bottom of the ship) and its waterline
	  */
	public BigDecimal getDraftMeter()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DraftMeter);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set GRT.
		@param GRT GRT (Gross Register Tonnage) is a historic unit that measures a ship&#039;s total internal volume.
	*/
	public void setGRT (BigDecimal GRT)
	{
		set_Value (COLUMNNAME_GRT, GRT);
	}

	/** Get GRT.
		@return GRT (Gross Register Tonnage) is a historic unit that measures a ship&#039;s total internal volume.
	  */
	public BigDecimal getGRT()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GRT);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IMO Number.
		@param IMONumber International Maritime Organization number
	*/
	public void setIMONumber (String IMONumber)
	{
		set_Value (COLUMNNAME_IMONumber, IMONumber);
	}

	/** Get IMO Number.
		@return International Maritime Organization number
	  */
	public String getIMONumber()
	{
		return (String)get_Value(COLUMNNAME_IMONumber);
	}

	/** Set LOA.
		@param LOA Length Overall (LOA): The total length of the ship from the tip of the bow to the stern.
	*/
	public void setLOA (int LOA)
	{
		set_Value (COLUMNNAME_LOA, Integer.valueOf(LOA));
	}

	/** Get LOA.
		@return Length Overall (LOA): The total length of the ship from the tip of the bow to the stern.
	  */
	public int getLOA()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LOA);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set NRT.
		@param NRT NRT (Net Register Tonnage) is a historic unit that measures a ship&#8217;s useful cargo-carrying internal volume.
	*/
	public void setNRT (BigDecimal NRT)
	{
		set_Value (COLUMNNAME_NRT, NRT);
	}

	/** Get NRT.
		@return NRT (Net Register Tonnage) is a historic unit that measures a ship&#8217;s useful cargo-carrying internal volume.
	  */
	public BigDecimal getNRT()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_NRT);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Vessel.
		@param STEV_Vessel_ID Vessel
	*/
	public void setSTEV_Vessel_ID (int STEV_Vessel_ID)
	{
		if (STEV_Vessel_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_Vessel_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_Vessel_ID, Integer.valueOf(STEV_Vessel_ID));
	}

	/** Get Vessel.
		@return Vessel	  */
	public int getSTEV_Vessel_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_Vessel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Barge = Barge */
	public static final String VESSELTYPE_Barge = "Barge";
	/** Bulk Carriers = Bulk Carriers */
	public static final String VESSELTYPE_BulkCarriers = "Bulk Carriers";
	/** Container Ships = Container Ships */
	public static final String VESSELTYPE_ContainerShips = "Container Ships";
	/** Passenger Ships = Passenger Ships */
	public static final String VESSELTYPE_PassengerShips = "Passenger Ships";
	/** Tankers = Tankers */
	public static final String VESSELTYPE_Tankers = "Tankers";
	/** Set VesselType.
		@param VesselType VesselType
	*/
	public void setVesselType (String VesselType)
	{

		set_Value (COLUMNNAME_VesselType, VesselType);
	}

	/** Get VesselType.
		@return VesselType	  */
	public String getVesselType()
	{
		return (String)get_Value(COLUMNNAME_VesselType);
	}
}