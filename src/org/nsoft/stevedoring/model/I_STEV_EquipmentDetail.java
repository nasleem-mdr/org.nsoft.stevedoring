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
package org.nsoft.stevedoring.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for STEV_EquipmentDetail
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_STEV_EquipmentDetail 
{

    /** TableName=STEV_EquipmentDetail */
    public static final String Table_Name = "STEV_EquipmentDetail";

    /** AD_Table_ID=1000031 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner.
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner.
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name CapacityTon */
    public static final String COLUMNNAME_CapacityTon = "CapacityTon";

	/** Set CapacityTon	  */
	public void setCapacityTon (BigDecimal CapacityTon);

	/** Get CapacityTon	  */
	public BigDecimal getCapacityTon();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name ManufactureYear */
    public static final String COLUMNNAME_ManufactureYear = "ManufactureYear";

	/** Set ManufactureYear	  */
	public void setManufactureYear (BigDecimal ManufactureYear);

	/** Get ManufactureYear	  */
	public BigDecimal getManufactureYear();

    /** Column name OwnershipType */
    public static final String COLUMNNAME_OwnershipType = "OwnershipType";

	/** Set OwnershipType	  */
	public void setOwnershipType (String OwnershipType);

	/** Get OwnershipType	  */
	public String getOwnershipType();

    /** Column name RentalContractNo */
    public static final String COLUMNNAME_RentalContractNo = "RentalContractNo";

	/** Set RentalContractNo	  */
	public void setRentalContractNo (String RentalContractNo);

	/** Get RentalContractNo	  */
	public String getRentalContractNo();

    /** Column name RentalEndDate */
    public static final String COLUMNNAME_RentalEndDate = "RentalEndDate";

	/** Set RentalEndDate	  */
	public void setRentalEndDate (Timestamp RentalEndDate);

	/** Get RentalEndDate	  */
	public Timestamp getRentalEndDate();

    /** Column name RentalRatePerDay */
    public static final String COLUMNNAME_RentalRatePerDay = "RentalRatePerDay";

	/** Set RentalRatePerDay	  */
	public void setRentalRatePerDay (BigDecimal RentalRatePerDay);

	/** Get RentalRatePerDay	  */
	public BigDecimal getRentalRatePerDay();

    /** Column name RentalStartDate */
    public static final String COLUMNNAME_RentalStartDate = "RentalStartDate";

	/** Set RentalStartDate	  */
	public void setRentalStartDate (Timestamp RentalStartDate);

	/** Get RentalStartDate	  */
	public Timestamp getRentalStartDate();

    /** Column name STEV_EquipmentDetail_ID */
    public static final String COLUMNNAME_STEV_EquipmentDetail_ID = "STEV_EquipmentDetail_ID";

	/** Set STEV_EquipmentDetail	  */
	public void setSTEV_EquipmentDetail_ID (int STEV_EquipmentDetail_ID);

	/** Get STEV_EquipmentDetail	  */
	public int getSTEV_EquipmentDetail_ID();

    /** Column name S_Resource_ID */
    public static final String COLUMNNAME_S_Resource_ID = "S_Resource_ID";

	/** Set Resource.
	  * Resource
	  */
	public void setS_Resource_ID (int S_Resource_ID);

	/** Get Resource.
	  * Resource
	  */
	public int getS_Resource_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_S_Resource getS_Resource() throws RuntimeException;

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
