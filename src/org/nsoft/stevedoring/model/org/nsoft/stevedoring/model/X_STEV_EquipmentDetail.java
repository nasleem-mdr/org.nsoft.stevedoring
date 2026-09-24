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

/** Generated Model for STEV_EquipmentDetail
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_EquipmentDetail")
public class X_STEV_EquipmentDetail extends PO implements I_STEV_EquipmentDetail, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260923L;

    /** Standard Constructor */
    public X_STEV_EquipmentDetail (Properties ctx, int STEV_EquipmentDetail_ID, String trxName)
    {
      super (ctx, STEV_EquipmentDetail_ID, trxName);
      /** if (STEV_EquipmentDetail_ID == 0)
        {
			setOwnershipType (null);
			setSTEV_EquipmentDetail_ID (0);
			setS_Resource_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_EquipmentDetail (Properties ctx, int STEV_EquipmentDetail_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_EquipmentDetail_ID, trxName, virtualColumns);
      /** if (STEV_EquipmentDetail_ID == 0)
        {
			setOwnershipType (null);
			setSTEV_EquipmentDetail_ID (0);
			setS_Resource_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_EquipmentDetail (Properties ctx, String STEV_EquipmentDetail_UU, String trxName)
    {
      super (ctx, STEV_EquipmentDetail_UU, trxName);
      /** if (STEV_EquipmentDetail_UU == null)
        {
			setOwnershipType (null);
			setSTEV_EquipmentDetail_ID (0);
			setS_Resource_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_EquipmentDetail (Properties ctx, String STEV_EquipmentDetail_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_EquipmentDetail_UU, trxName, virtualColumns);
      /** if (STEV_EquipmentDetail_UU == null)
        {
			setOwnershipType (null);
			setSTEV_EquipmentDetail_ID (0);
			setS_Resource_ID (0);
        } */
    }

    /** Load Constructor */
    public X_STEV_EquipmentDetail (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_EquipmentDetail[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException
	{
		return (org.compiere.model.I_A_Asset)MTable.get(getCtx(), org.compiere.model.I_A_Asset.Table_ID)
			.getPO(getA_Asset_ID(), get_TrxName());
	}

	/** Set Asset.
		@param A_Asset_ID Asset used internally or by customers
	*/
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1)
			set_Value (COLUMNNAME_A_Asset_ID, null);
		else
			set_Value (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
	{
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_ID)
			.getPO(getC_Currency_ID(), get_TrxName());
	}

	/** Set Currency.
		@param C_Currency_ID The Currency for this record
	*/
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1)
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CapacityTon.
		@param CapacityTon CapacityTon
	*/
	public void setCapacityTon (BigDecimal CapacityTon)
	{
		set_Value (COLUMNNAME_CapacityTon, CapacityTon);
	}

	/** Get CapacityTon.
		@return CapacityTon	  */
	public BigDecimal getCapacityTon()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CapacityTon);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set ManufactureYear.
		@param ManufactureYear ManufactureYear
	*/
	public void setManufactureYear (BigDecimal ManufactureYear)
	{
		set_Value (COLUMNNAME_ManufactureYear, ManufactureYear);
	}

	/** Get ManufactureYear.
		@return ManufactureYear	  */
	public BigDecimal getManufactureYear()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ManufactureYear);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Own = Own */
	public static final String OWNERSHIPTYPE_Own = "Own";
	/** Rental = Rent */
	public static final String OWNERSHIPTYPE_Rental = "Rent";
	/** Set Ownership Type.
		@param OwnershipType Ownership Type
	*/
	public void setOwnershipType (String OwnershipType)
	{

		set_Value (COLUMNNAME_OwnershipType, OwnershipType);
	}

	/** Get Ownership Type.
		@return Ownership Type	  */
	public String getOwnershipType()
	{
		return (String)get_Value(COLUMNNAME_OwnershipType);
	}

	/** Set Rental Contract No.
		@param RentalContractNo Rental Contract No
	*/
	public void setRentalContractNo (String RentalContractNo)
	{
		set_Value (COLUMNNAME_RentalContractNo, RentalContractNo);
	}

	/** Get Rental Contract No.
		@return Rental Contract No	  */
	public String getRentalContractNo()
	{
		return (String)get_Value(COLUMNNAME_RentalContractNo);
	}

	/** Set Rental End Date.
		@param RentalEndDate Rental End Date
	*/
	public void setRentalEndDate (Timestamp RentalEndDate)
	{
		set_Value (COLUMNNAME_RentalEndDate, RentalEndDate);
	}

	/** Get Rental End Date.
		@return Rental End Date	  */
	public Timestamp getRentalEndDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_RentalEndDate);
	}

	/** Set Rental Rate Per Day.
		@param RentalRatePerDay Rental Rate Per Day
	*/
	public void setRentalRatePerDay (BigDecimal RentalRatePerDay)
	{
		set_Value (COLUMNNAME_RentalRatePerDay, RentalRatePerDay);
	}

	/** Get Rental Rate Per Day.
		@return Rental Rate Per Day	  */
	public BigDecimal getRentalRatePerDay()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_RentalRatePerDay);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rental Start Date.
		@param RentalStartDate Rental Start Date
	*/
	public void setRentalStartDate (Timestamp RentalStartDate)
	{
		set_Value (COLUMNNAME_RentalStartDate, RentalStartDate);
	}

	/** Get Rental Start Date.
		@return Rental Start Date	  */
	public Timestamp getRentalStartDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_RentalStartDate);
	}

	/** Set Equipment Detail.
		@param STEV_EquipmentDetail_ID Equipment Detail
	*/
	public void setSTEV_EquipmentDetail_ID (int STEV_EquipmentDetail_ID)
	{
		if (STEV_EquipmentDetail_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_EquipmentDetail_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_EquipmentDetail_ID, Integer.valueOf(STEV_EquipmentDetail_ID));
	}

	/** Get Equipment Detail.
		@return Equipment Detail	  */
	public int getSTEV_EquipmentDetail_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_EquipmentDetail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_S_Resource getS_Resource() throws RuntimeException
	{
		return (org.compiere.model.I_S_Resource)MTable.get(getCtx(), org.compiere.model.I_S_Resource.Table_ID)
			.getPO(getS_Resource_ID(), get_TrxName());
	}

	/** Set Resource.
		@param S_Resource_ID Resource
	*/
	public void setS_Resource_ID (int S_Resource_ID)
	{
		if (S_Resource_ID < 1)
			set_ValueNoCheck (COLUMNNAME_S_Resource_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_S_Resource_ID, Integer.valueOf(S_Resource_ID));
	}

	/** Get Resource.
		@return Resource
	  */
	public int getS_Resource_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_S_Resource_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}