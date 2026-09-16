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

/** Generated Interface for STEV_VesselSchedule
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_STEV_VesselSchedule 
{

    /** TableName=STEV_VesselSchedule */
    public static final String Table_Name = "STEV_VesselSchedule";

    /** AD_Table_ID=1000027 */
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

    /** Column name ATA */
    public static final String COLUMNNAME_ATA = "ATA";

	/** Set ATA.
	  * Actual Time of Arrival
	  */
	public void setATA (Timestamp ATA);

	/** Get ATA.
	  * Actual Time of Arrival
	  */
	public Timestamp getATA();

    /** Column name ATB */
    public static final String COLUMNNAME_ATB = "ATB";

	/** Set ATB.
	  * Actual Time of Berthing
	  */
	public void setATB (Timestamp ATB);

	/** Get ATB.
	  * Actual Time of Berthing
	  */
	public Timestamp getATB();

    /** Column name ATD */
    public static final String COLUMNNAME_ATD = "ATD";

	/** Set ATD.
	  * Actual Time of Departure
	  */
	public void setATD (Timestamp ATD);

	/** Get ATD.
	  * Actual Time of Departure
	  */
	public Timestamp getATD();

    /** Column name ActivityType */
    public static final String COLUMNNAME_ActivityType = "ActivityType";

	/** Set ActivityType	  */
	public void setActivityType (String ActivityType);

	/** Get ActivityType	  */
	public String getActivityType();

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

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException;

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

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name ETA */
    public static final String COLUMNNAME_ETA = "ETA";

	/** Set ETA.
	  * Estimated Time of Arrival
	  */
	public void setETA (Timestamp ETA);

	/** Get ETA.
	  * Estimated Time of Arrival
	  */
	public Timestamp getETA();

    /** Column name ETB */
    public static final String COLUMNNAME_ETB = "ETB";

	/** Set ETB.
	  * Estimated Time of Berthing
	  */
	public void setETB (Timestamp ETB);

	/** Get ETB.
	  * Estimated Time of Berthing
	  */
	public Timestamp getETB();

    /** Column name ETD */
    public static final String COLUMNNAME_ETD = "ETD";

	/** Set ETD.
	  * Estimated Time of Departure 
	  */
	public void setETD (Timestamp ETD);

	/** Get ETD.
	  * Estimated Time of Departure 
	  */
	public Timestamp getETD();

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

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name STEV_Berth_ID */
    public static final String COLUMNNAME_STEV_Berth_ID = "STEV_Berth_ID";

	/** Set STEV_Berth	  */
	public void setSTEV_Berth_ID (int STEV_Berth_ID);

	/** Get STEV_Berth	  */
	public int getSTEV_Berth_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_Berth getSTEV_Berth() throws RuntimeException;

    /** Column name STEV_VesselSchedule_ID */
    public static final String COLUMNNAME_STEV_VesselSchedule_ID = "STEV_VesselSchedule_ID";

	/** Set STEV_VesselSchedule	  */
	public void setSTEV_VesselSchedule_ID (int STEV_VesselSchedule_ID);

	/** Get STEV_VesselSchedule	  */
	public int getSTEV_VesselSchedule_ID();

    /** Column name STEV_Vessel_ID */
    public static final String COLUMNNAME_STEV_Vessel_ID = "STEV_Vessel_ID";

	/** Set STEV_Vessel	  */
	public void setSTEV_Vessel_ID (int STEV_Vessel_ID);

	/** Get STEV_Vessel	  */
	public int getSTEV_Vessel_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_Vessel getSTEV_Vessel() throws RuntimeException;

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
