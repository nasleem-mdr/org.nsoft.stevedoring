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

/** Generated Model for STEV_Berth
 *  @author iDempiere (generated)
 *  @version Release 13 - $Id$ */
@org.adempiere.base.Model(table="STEV_Berth")
public class X_STEV_Berth extends PO implements I_STEV_Berth, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260915L;

    /** Standard Constructor */
    public X_STEV_Berth (Properties ctx, int STEV_Berth_ID, String trxName)
    {
      super (ctx, STEV_Berth_ID, trxName);
      /** if (STEV_Berth_ID == 0)
        {
			setName (null);
			setSTEV_Berth_ID (0);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_STEV_Berth (Properties ctx, int STEV_Berth_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_Berth_ID, trxName, virtualColumns);
      /** if (STEV_Berth_ID == 0)
        {
			setName (null);
			setSTEV_Berth_ID (0);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_STEV_Berth (Properties ctx, String STEV_Berth_UU, String trxName)
    {
      super (ctx, STEV_Berth_UU, trxName);
      /** if (STEV_Berth_UU == null)
        {
			setName (null);
			setSTEV_Berth_ID (0);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_STEV_Berth (Properties ctx, String STEV_Berth_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, STEV_Berth_UU, trxName, virtualColumns);
      /** if (STEV_Berth_UU == null)
        {
			setName (null);
			setSTEV_Berth_ID (0);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_STEV_Berth (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_STEV_Berth[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set DepthMeter.
		@param DepthMeter DepthMeter
	*/
	public void setDepthMeter (BigDecimal DepthMeter)
	{
		set_Value (COLUMNNAME_DepthMeter, DepthMeter);
	}

	/** Get DepthMeter.
		@return DepthMeter	  */
	public BigDecimal getDepthMeter()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DepthMeter);
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

	/** Set LengthMeter.
		@param LengthMeter LengthMeter
	*/
	public void setLengthMeter (BigDecimal LengthMeter)
	{
		set_Value (COLUMNNAME_LengthMeter, LengthMeter);
	}

	/** Get LengthMeter.
		@return LengthMeter	  */
	public BigDecimal getLengthMeter()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LengthMeter);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MaxDWT.
		@param MaxDWT MaxDWT
	*/
	public void setMaxDWT (BigDecimal MaxDWT)
	{
		set_Value (COLUMNNAME_MaxDWT, MaxDWT);
	}

	/** Get MaxDWT.
		@return MaxDWT	  */
	public BigDecimal getMaxDWT()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MaxDWT);
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

	/** Set STEV_Berth.
		@param STEV_Berth_ID STEV_Berth
	*/
	public void setSTEV_Berth_ID (int STEV_Berth_ID)
	{
		if (STEV_Berth_ID < 1)
			set_ValueNoCheck (COLUMNNAME_STEV_Berth_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_STEV_Berth_ID, Integer.valueOf(STEV_Berth_ID));
	}

	/** Get STEV_Berth.
		@return STEV_Berth	  */
	public int getSTEV_Berth_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_STEV_Berth_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}