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

/** Generated Model for STEV_TallyLine
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_TallyLine")
public class X_STEV_TallyLine extends PO implements I_STEV_TallyLine, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260916L;

    /** Standard Constructor */
    public X_STEV_TallyLine (Properties ctx, int STEV_TallyLine_ID, String trxName)
    {
      super (ctx, STEV_TallyLine_ID, trxName);
      /** if (STEV_TallyLine_ID == 0)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyMoved (Env.ZERO);
			setSTEV_TallyLine_ID (0);
			setSTEV_TallySheet_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_TallyLine (Properties ctx, int STEV_TallyLine_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_TallyLine_ID, trxName, virtualColumns);
      /** if (STEV_TallyLine_ID == 0)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyMoved (Env.ZERO);
			setSTEV_TallyLine_ID (0);
			setSTEV_TallySheet_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_TallyLine (Properties ctx, String STEV_TallyLine_UU, String trxName)
    {
      super (ctx, STEV_TallyLine_UU, trxName);
      /** if (STEV_TallyLine_UU == null)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyMoved (Env.ZERO);
			setSTEV_TallyLine_ID (0);
			setSTEV_TallySheet_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_TallyLine (Properties ctx, String STEV_TallyLine_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_TallyLine_UU, trxName, virtualColumns);
      /** if (STEV_TallyLine_UU == null)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyMoved (Env.ZERO);
			setSTEV_TallyLine_ID (0);
			setSTEV_TallySheet_ID (0);
        } */
    }

    /** Load Constructor */
    public X_STEV_TallyLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_TallyLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException
	{
		return (org.compiere.model.I_C_UOM)MTable.get(getCtx(), org.compiere.model.I_C_UOM.Table_ID)
			.getPO(getC_UOM_ID(), get_TrxName());
	}

	/** Set UOM.
		@param C_UOM_ID Unit of Measure
	*/
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1)
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
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

	/** Set HatchNo.
		@param HatchNo HatchNo
	*/
	public void setHatchNo (String HatchNo)
	{
		set_Value (COLUMNNAME_HatchNo, HatchNo);
	}

	/** Get HatchNo.
		@return HatchNo	  */
	public String getHatchNo()
	{
		return (String)get_Value(COLUMNNAME_HatchNo);
	}

	/** Set Line No.
		@param Line Unique line for this document
	*/
	public void setLine (int Line)
	{
		set_ValueNoCheck (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
	{
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_ID)
			.getPO(getM_Product_ID(), get_TrxName());
	}

	/** Set Product.
		@param M_Product_ID Product, Service, Item
	*/
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1)
			set_Value (COLUMNNAME_M_Product_ID, null);
		else
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyMoved.
		@param QtyMoved QtyMoved
	*/
	public void setQtyMoved (BigDecimal QtyMoved)
	{
		set_Value (COLUMNNAME_QtyMoved, QtyMoved);
	}

	/** Get QtyMoved.
		@return QtyMoved	  */
	public BigDecimal getQtyMoved()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyMoved);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set STEV_TallyLine.
		@param STEV_TallyLine_ID STEV_TallyLine
	*/
	public void setSTEV_TallyLine_ID (int STEV_TallyLine_ID)
	{
		if (STEV_TallyLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_TallyLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_TallyLine_ID, Integer.valueOf(STEV_TallyLine_ID));
	}

	/** Get STEV_TallyLine.
		@return STEV_TallyLine	  */
	public int getSTEV_TallyLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_TallyLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_TallySheet getSTEV_TallySheet() throws RuntimeException
	{
		return (I_STEV_TallySheet)MTable.get(getCtx(), I_STEV_TallySheet.Table_ID)
			.getPO(getSTEV_TallySheet_ID(), get_TrxName());
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

	/** Set TimeEnd.
		@param TimeEnd TimeEnd
	*/
	public void setTimeEnd (Timestamp TimeEnd)
	{
		set_Value (COLUMNNAME_TimeEnd, TimeEnd);
	}

	/** Get TimeEnd.
		@return TimeEnd	  */
	public Timestamp getTimeEnd()
	{
		return (Timestamp)get_Value(COLUMNNAME_TimeEnd);
	}

	/** Set TimeStart.
		@param TimeStart TimeStart
	*/
	public void setTimeStart (Timestamp TimeStart)
	{
		set_Value (COLUMNNAME_TimeStart, TimeStart);
	}

	/** Get TimeStart.
		@return TimeStart	  */
	public Timestamp getTimeStart()
	{
		return (Timestamp)get_Value(COLUMNNAME_TimeStart);
	}
}