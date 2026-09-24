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

/** Generated Interface for STEV_StatementOfFact
 *  @author iDempiere (generated) 
 *  @version Release 13
 */
@SuppressWarnings("all")
public interface I_STEV_StatementOfFact 
{

    /** TableName=STEV_StatementOfFact */
    public static final String Table_Name = "STEV_StatementOfFact";

    /** AD_Table_ID=1000030 */
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

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

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

    /** Column name CompleteButton */
    public static final String COLUMNNAME_CompleteButton = "CompleteButton";

	/** Set CompleteButton	  */
	public void setCompleteButton (String CompleteButton);

	/** Get CompleteButton	  */
	public String getCompleteButton();

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

    /** Column name DigitalSignature */
    public static final String COLUMNNAME_DigitalSignature = "DigitalSignature";

	/** Set DigitalSignature	  */
	public void setDigitalSignature (String DigitalSignature);

	/** Get DigitalSignature	  */
	public String getDigitalSignature();

    /** Column name DocAction */
    public static final String COLUMNNAME_DocAction = "DocAction";

	/** Set Document Action.
	  * The targeted status of the document
	  */
	public void setDocAction (String DocAction);

	/** Get Document Action.
	  * The targeted status of the document
	  */
	public String getDocAction();

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

    /** Column name IdleTimeReason */
    public static final String COLUMNNAME_IdleTimeReason = "IdleTimeReason";

	/** Set IdleTimeReason	  */
	public void setIdleTimeReason (String IdleTimeReason);

	/** Get IdleTimeReason	  */
	public String getIdleTimeReason();

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

    /** Column name IsApproved */
    public static final String COLUMNNAME_IsApproved = "IsApproved";

	/** Set Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name MasterName */
    public static final String COLUMNNAME_MasterName = "MasterName";

	/** Set MasterName	  */
	public void setMasterName (String MasterName);

	/** Get MasterName	  */
	public String getMasterName();

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

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name STEV_StatementOfFact_ID */
    public static final String COLUMNNAME_STEV_StatementOfFact_ID = "STEV_StatementOfFact_ID";

	/** Set STEV_StatementOfFact	  */
	public void setSTEV_StatementOfFact_ID (int STEV_StatementOfFact_ID);

	/** Get STEV_StatementOfFact	  */
	public int getSTEV_StatementOfFact_ID();

    /** Column name STEV_VesselSchedule_ID */
    public static final String COLUMNNAME_STEV_VesselSchedule_ID = "STEV_VesselSchedule_ID";

	/** Set STEV_VesselSchedule	  */
	public void setSTEV_VesselSchedule_ID (int STEV_VesselSchedule_ID);

	/** Get STEV_VesselSchedule	  */
	public int getSTEV_VesselSchedule_ID();

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_VesselSchedule getSTEV_VesselSchedule() throws RuntimeException;

    /** Column name SignedDate */
    public static final String COLUMNNAME_SignedDate = "SignedDate";

	/** Set SignedDate	  */
	public void setSignedDate (Timestamp SignedDate);

	/** Get SignedDate	  */
	public Timestamp getSignedDate();

    /** Column name TotalIdleTimeMinutes */
    public static final String COLUMNNAME_TotalIdleTimeMinutes = "TotalIdleTimeMinutes";

	/** Set TotalIdleTimeMinutes	  */
	public void setTotalIdleTimeMinutes (int TotalIdleTimeMinutes);

	/** Get TotalIdleTimeMinutes	  */
	public int getTotalIdleTimeMinutes();

    /** Column name TotalQtyRealized */
    public static final String COLUMNNAME_TotalQtyRealized = "TotalQtyRealized";

	/** Set TotalQtyRealized	  */
	public void setTotalQtyRealized (BigDecimal TotalQtyRealized);

	/** Get TotalQtyRealized	  */
	public BigDecimal getTotalQtyRealized();

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
