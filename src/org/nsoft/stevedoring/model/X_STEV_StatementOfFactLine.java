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

/** Generated Model for STEV_StatementOfFactLine
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_StatementOfFactLine")
public class X_STEV_StatementOfFactLine extends PO implements I_STEV_StatementOfFactLine, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260917L;

    /** Standard Constructor */
    public X_STEV_StatementOfFactLine (Properties ctx, int STEV_StatementOfFactLine_ID, String trxName)
    {
      super (ctx, STEV_StatementOfFactLine_ID, trxName);
      /** if (STEV_StatementOfFactLine_ID == 0)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyRealized (Env.ZERO);
			setSTEV_StatementOfFactLine_ID (0);
			setSTEV_StatementOfFact_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_StatementOfFactLine (Properties ctx, int STEV_StatementOfFactLine_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_StatementOfFactLine_ID, trxName, virtualColumns);
      /** if (STEV_StatementOfFactLine_ID == 0)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyRealized (Env.ZERO);
			setSTEV_StatementOfFactLine_ID (0);
			setSTEV_StatementOfFact_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_StatementOfFactLine (Properties ctx, String STEV_StatementOfFactLine_UU, String trxName)
    {
      super (ctx, STEV_StatementOfFactLine_UU, trxName);
      /** if (STEV_StatementOfFactLine_UU == null)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyRealized (Env.ZERO);
			setSTEV_StatementOfFactLine_ID (0);
			setSTEV_StatementOfFact_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_STEV_StatementOfFactLine (Properties ctx, String STEV_StatementOfFactLine_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_StatementOfFactLine_UU, trxName, virtualColumns);
      /** if (STEV_StatementOfFactLine_UU == null)
        {
			setC_UOM_ID (0);
			setLine (0);
			setM_Product_ID (0);
			setQtyRealized (Env.ZERO);
			setSTEV_StatementOfFactLine_ID (0);
			setSTEV_StatementOfFact_ID (0);
        } */
    }

    /** Load Constructor */
    public X_STEV_StatementOfFactLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_StatementOfFactLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	@Deprecated(since="13") // use better methods with cache
	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException
	{
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_ID)
			.getPO(getC_OrderLine_ID(), get_TrxName());
	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID Sales Order Line
	*/
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
	public org.compiere.model.I_M_InOutLine getM_InOutLine() throws RuntimeException
	{
		return (org.compiere.model.I_M_InOutLine)MTable.get(getCtx(), org.compiere.model.I_M_InOutLine.Table_ID)
			.getPO(getM_InOutLine_ID(), get_TrxName());
	}

	/** Set Shipment/Receipt Line.
		@param M_InOutLine_ID Line on Shipment or Receipt document
	*/
	public void setM_InOutLine_ID (int M_InOutLine_ID)
	{
		if (M_InOutLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_M_InOutLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_InOutLine_ID, Integer.valueOf(M_InOutLine_ID));
	}

	/** Get Shipment/Receipt Line.
		@return Line on Shipment or Receipt document
	  */
	public int getM_InOutLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InOutLine_ID);
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

	/** Set QtyRealized.
		@param QtyRealized QtyRealized
	*/
	public void setQtyRealized (BigDecimal QtyRealized)
	{
		set_Value (COLUMNNAME_QtyRealized, QtyRealized);
	}

	/** Get QtyRealized.
		@return QtyRealized	  */
	public BigDecimal getQtyRealized()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyRealized);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set STEV_StatementOfFactLine.
		@param STEV_StatementOfFactLine_ID STEV_StatementOfFactLine
	*/
	public void setSTEV_StatementOfFactLine_ID (int STEV_StatementOfFactLine_ID)
	{
		if (STEV_StatementOfFactLine_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_StatementOfFactLine_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_StatementOfFactLine_ID, Integer.valueOf(STEV_StatementOfFactLine_ID));
	}

	/** Get STEV_StatementOfFactLine.
		@return STEV_StatementOfFactLine	  */
	public int getSTEV_StatementOfFactLine_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_StatementOfFactLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	@Deprecated(since="13") // use better methods with cache
	public I_STEV_StatementOfFact getSTEV_StatementOfFact() throws RuntimeException
	{
		return (I_STEV_StatementOfFact)MTable.get(getCtx(), I_STEV_StatementOfFact.Table_ID)
			.getPO(getSTEV_StatementOfFact_ID(), get_TrxName());
	}

	/** Set STEV_StatementOfFact.
		@param STEV_StatementOfFact_ID STEV_StatementOfFact
	*/
	public void setSTEV_StatementOfFact_ID (int STEV_StatementOfFact_ID)
	{
		if (STEV_StatementOfFact_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_StatementOfFact_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_StatementOfFact_ID, Integer.valueOf(STEV_StatementOfFact_ID));
	}

	/** Get STEV_StatementOfFact.
		@return STEV_StatementOfFact	  */
	public int getSTEV_StatementOfFact_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_StatementOfFact_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}