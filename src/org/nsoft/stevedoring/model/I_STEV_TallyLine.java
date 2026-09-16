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

/** Generated Interface for STEV_TallyLine
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_STEV_TallyLine 
{

    /** TableName=STEV_TallyLine */
    public static final String Table_Name = "STEV_TallyLine";

    /** AD_Table_ID=1000029 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

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

    /** Column name C_UOM_ID */
    public static final String COLUMNNAME_C_UOM_ID = "C_UOM_ID";

	/** Set UOM.
	  * Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID);

	/** Get UOM.
	  * Unit of Measure
	  */
	public int getC_UOM_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException;

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

    /** Column name HatchNo */
    public static final String COLUMNNAME_HatchNo = "HatchNo";

	/** Set HatchNo	  */
	public void setHatchNo (String HatchNo);

	/** Get HatchNo	  */
	public String getHatchNo();

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

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

	/** Set Line No.
	  * Unique line for this document
	  */
	public void setLine (int Line);

	/** Get Line No.
	  * Unique line for this document
	  */
	public int getLine();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name QtyMoved */
    public static final String COLUMNNAME_QtyMoved = "QtyMoved";

	/** Set QtyMoved	  */
	public void setQtyMoved (BigDecimal QtyMoved);

	/** Get QtyMoved	  */
	public BigDecimal getQtyMoved();

    /** Column name STEV_TallyLine_ID */
    public static final String COLUMNNAME_STEV_TallyLine_ID = "STEV_TallyLine_ID";

	/** Set STEV_TallyLine	  */
	public void setSTEV_TallyLine_ID (int STEV_TallyLine_ID);

	/** Get STEV_TallyLine	  */
	public int getSTEV_TallyLine_ID();

    /** Column name STEV_TallySheet_ID */
    public static final String COLUMNNAME_STEV_TallySheet_ID = "STEV_TallySheet_ID";

	/** Set STEV_TallySheet	  */
	public void setSTEV_TallySheet_ID (int STEV_TallySheet_ID);

	/** Get STEV_TallySheet	  */
	public int getSTEV_TallySheet_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_TallySheet getSTEV_TallySheet() throws RuntimeException;

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

    /** Column name TimeEnd */
    public static final String COLUMNNAME_TimeEnd = "TimeEnd";

	/** Set TimeEnd	  */
	public void setTimeEnd (Timestamp TimeEnd);

	/** Get TimeEnd	  */
	public Timestamp getTimeEnd();

    /** Column name TimeStart */
    public static final String COLUMNNAME_TimeStart = "TimeStart";

	/** Set TimeStart	  */
	public void setTimeStart (Timestamp TimeStart);

	/** Get TimeStart	  */
	public Timestamp getTimeStart();

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
