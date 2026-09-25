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

/** Generated Interface for STEV_Vessel
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_STEV_Vessel 
{

    /** TableName=STEV_Vessel */
    public static final String Table_Name = "STEV_Vessel";

    /** AD_Table_ID=1000013 */
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

    /** Column name Beam */
    public static final String COLUMNNAME_Beam = "Beam";

	/** Set Beam.
	  * The overall width of a vessel at its widest point, measured from the outermost edge of the hull on one side to the outermost edge on the other
	  */
	public void setBeam (BigDecimal Beam);

	/** Get Beam.
	  * The overall width of a vessel at its widest point, measured from the outermost edge of the hull on one side to the outermost edge on the other
	  */
	public BigDecimal getBeam();

    /** Column name C_Country_ID */
    public static final String COLUMNNAME_C_Country_ID = "C_Country_ID";

	/** Set Flag Country.
	  * Flag Country 
	  */
	public void setC_Country_ID (int C_Country_ID);

	/** Get Flag Country.
	  * Flag Country 
	  */
	public int getC_Country_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_Country getC_Country() throws RuntimeException;

    /** Column name CallSign */
    public static final String COLUMNNAME_CallSign = "CallSign";

	/** Set Call Sign.
	  * A unique alphanumeric identifier assigned to a ship by its flag state&#039;
s telecommunications authority.
	  */
	public void setCallSign (String CallSign);

	/** Get Call Sign.
	  * A unique alphanumeric identifier assigned to a ship by its flag state&#039;
s telecommunications authority.
	  */
	public String getCallSign();

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

    /** Column name DraftMeter */
    public static final String COLUMNNAME_DraftMeter = "DraftMeter";

	/** Set DraftMeter.
	  * Measures the vertical distance between a vessel&#8217;
s keel (the absolute bottom of the ship) and its waterline
	  */
	public void setDraftMeter (BigDecimal DraftMeter);

	/** Get DraftMeter.
	  * Measures the vertical distance between a vessel&#8217;
s keel (the absolute bottom of the ship) and its waterline
	  */
	public BigDecimal getDraftMeter();

    /** Column name GRT */
    public static final String COLUMNNAME_GRT = "GRT";

	/** Set GRT.
	  * GRT (Gross Register Tonnage) is a historic unit that measures a ship&#039;
s total internal volume.
	  */
	public void setGRT (BigDecimal GRT);

	/** Get GRT.
	  * GRT (Gross Register Tonnage) is a historic unit that measures a ship&#039;
s total internal volume.
	  */
	public BigDecimal getGRT();

    /** Column name IMONumber */
    public static final String COLUMNNAME_IMONumber = "IMONumber";

	/** Set IMO Number.
	  * International Maritime Organization number
	  */
	public void setIMONumber (String IMONumber);

	/** Get IMO Number.
	  * International Maritime Organization number
	  */
	public String getIMONumber();

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

    /** Column name LOA */
    public static final String COLUMNNAME_LOA = "LOA";

	/** Set LOA.
	  * Length Overall (LOA): The total length of the ship from the tip of the bow to the stern.
	  */
	public void setLOA (BigDecimal LOA);

	/** Get LOA.
	  * Length Overall (LOA): The total length of the ship from the tip of the bow to the stern.
	  */
	public BigDecimal getLOA();

    /** Column name NRT */
    public static final String COLUMNNAME_NRT = "NRT";

	/** Set NRT.
	  * NRT (Net Register Tonnage) is a historic unit that measures a ship&#8217;
s useful cargo-carrying internal volume.
	  */
	public void setNRT (BigDecimal NRT);

	/** Get NRT.
	  * NRT (Net Register Tonnage) is a historic unit that measures a ship&#8217;
s useful cargo-carrying internal volume.
	  */
	public BigDecimal getNRT();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name STEV_Vessel_ID */
    public static final String COLUMNNAME_STEV_Vessel_ID = "STEV_Vessel_ID";

	/** Set Vessel	  */
	public void setSTEV_Vessel_ID (int STEV_Vessel_ID);

	/** Get Vessel	  */
	public int getSTEV_Vessel_ID();

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

    /** Column name VesselType */
    public static final String COLUMNNAME_VesselType = "VesselType";

	/** Set VesselType	  */
	public void setVesselType (String VesselType);

	/** Get VesselType	  */
	public String getVesselType();
}
