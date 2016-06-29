/*
 * Copyright 2016 Red Eléctrica de España, S.A.U.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, version 3 of the license.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTIBIILTY or FITNESS FOR A PARTICULAR PURPOSE. See GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 *
 * Any redistribution and/or modification of this program has to make
 * reference to Red Eléctrica de España, S.A.U. as the copyright owner of
 * the program.
 */

package es.ree.eemws.core.utils.error;

import es.ree.eemws.core.utils.i18n.Messages; 

/**
 * Generic client error codes.
 * 
 * @author Red Eléctrica de España S.A.U.
 * @version 1.1 10/01/2016
 * 
 */
public enum EnumErrorCatalog implements ErrorCodes {
    
    /* See <code>ErrorCodes</code> for message description. */
     
    ERR_HAND_001(HAND_001, Messages.getString(HAND_001)),
    ERR_HAND_002(HAND_002, Messages.getString(HAND_002)),
    ERR_HAND_003(HAND_003, Messages.getString(HAND_003)),
    ERR_HAND_004(HAND_004, Messages.getString(HAND_004)),
    ERR_HAND_005(HAND_005, Messages.getString(HAND_005)),
    ERR_HAND_006(HAND_006, Messages.getString(HAND_006)),    
    ERR_HAND_007(HAND_007, Messages.getString(HAND_007)),  
    ERR_HAND_008(HAND_008, Messages.getString(HAND_008)),  
    ERR_HAND_009(HAND_009, Messages.getString(HAND_009)),  
    ERR_HAND_010(HAND_010, Messages.getString(HAND_010)),
    ERR_HAND_011(HAND_011, Messages.getString(HAND_011)),
    ERR_HAND_012(HAND_012, Messages.getString(HAND_012)),
    ERR_HAND_013(HAND_013, Messages.getString(HAND_013)),
    ERR_HAND_014(HAND_014, Messages.getString(HAND_014)),
    ERR_HAND_015(HAND_015, Messages.getString(HAND_015)),
    ERR_HAND_016(HAND_016, Messages.getString(HAND_016)),
    ERR_HAND_017(HAND_017, Messages.getString(HAND_017)),
    ERR_HAND_018(HAND_018, Messages.getString(HAND_018)),
    ERR_HAND_019(HAND_019, Messages.getString(HAND_019)),
    ERR_HAND_020(HAND_020, Messages.getString(HAND_020)),
    ERR_HAND_021(HAND_021, Messages.getString(HAND_021)),
    ERR_HAND_022(HAND_022, Messages.getString(HAND_022)),
    ERR_HAND_023(HAND_023, Messages.getString(HAND_023)),
        
    ERR_LST_001(LST_001, Messages.getString(LST_001)),
    ERR_LST_002(LST_002, Messages.getString(LST_002)),
    ERR_LST_003(LST_003, Messages.getString(LST_003)),
    ERR_LST_004(LST_004, Messages.getString(LST_004)),
    ERR_LST_005(LST_005, Messages.getString(LST_005)), 
    ERR_LST_006(LST_006, Messages.getString(LST_006)),
    ERR_LST_007(LST_007, Messages.getString(LST_007)),
    ERR_LST_008(LST_008, Messages.getString(LST_008)), 
    ERR_LST_009(LST_009, Messages.getString(LST_009)),
    ERR_LST_010(LST_010, Messages.getString(LST_010)), 
    ERR_LST_011(LST_011, Messages.getString(LST_011)),
    ERR_LST_012(LST_012, Messages.getString(LST_012)),
    ERR_LST_013(LST_013, Messages.getString(LST_013)),
    ERR_LST_014(LST_014, Messages.getString(LST_014)),
    ERR_LST_015(LST_015, Messages.getString(LST_015)),
    ERR_LST_016(LST_016, Messages.getString(LST_016)),
    ERR_LST_017(LST_017, Messages.getString(LST_017)),
    ERR_LST_018(LST_018, Messages.getString(LST_018)),
    ERR_LST_019(LST_019, Messages.getString(LST_019)),
    ERR_LST_020(LST_020, Messages.getString(LST_020)),
      
    ERR_GET_001(GET_001, Messages.getString(GET_001)),
    ERR_GET_002(GET_002, Messages.getString(GET_002)),
    ERR_GET_003(GET_003, Messages.getString(GET_003)),
    ERR_GET_004(GET_004, Messages.getString(GET_004)),
    ERR_GET_005(GET_005, Messages.getString(GET_005)),
    ERR_GET_006(GET_006, Messages.getString(GET_006)),
    ERR_GET_007(GET_007, Messages.getString(GET_007)),
    ERR_GET_008(GET_008, Messages.getString(GET_008)),
    ERR_GET_009(GET_009, Messages.getString(GET_009)),
    ERR_GET_010(GET_010, Messages.getString(GET_010)),
    ERR_GET_011(GET_011, Messages.getString(GET_011)),
    ERR_GET_012(GET_012, Messages.getString(GET_012)),
    ERR_GET_013(GET_013, Messages.getString(GET_013)),
    ERR_GET_014(GET_014, Messages.getString(GET_014)),
    ERR_GET_015(GET_015, Messages.getString(GET_015)),
    ERR_GET_016(GET_016, Messages.getString(GET_016)),
    ERR_GET_017(GET_017, Messages.getString(GET_017)),
    ERR_GET_018(GET_018, Messages.getString(GET_018)),
    ERR_GET_019(GET_019, Messages.getString(GET_019)),
    ERR_GET_020(GET_020, Messages.getString(GET_020)),
    ERR_GET_021(GET_021, Messages.getString(GET_021)),
           
    ERR_QRY_001(QRY_001, Messages.getString(QRY_001)),
    ERR_QRY_002(QRY_002, Messages.getString(QRY_002)),
    ERR_QRY_003(QRY_003, Messages.getString(QRY_003)),
    ERR_QRY_004(QRY_004, Messages.getString(QRY_004)),
    ERR_QRY_005(QRY_005, Messages.getString(QRY_005)),
    ERR_QRY_006(QRY_006, Messages.getString(QRY_006)),
    ERR_QRY_007(QRY_007, Messages.getString(QRY_007)),
    ERR_QRY_008(QRY_008, Messages.getString(QRY_008)),
    ERR_QRY_009(QRY_009, Messages.getString(QRY_009)),
    ERR_QRY_010(QRY_010, Messages.getString(QRY_010)),
    ERR_QRY_011(QRY_011, Messages.getString(QRY_011)),
    ERR_QRY_012(QRY_012, Messages.getString(QRY_012)),
    ERR_QRY_013(QRY_013, Messages.getString(QRY_013)),
         
    ERR_PUT_001(PUT_001, Messages.getString(PUT_001)),
    ERR_PUT_002(PUT_002, Messages.getString(PUT_002)),
    ERR_PUT_003(PUT_003, Messages.getString(PUT_003)),
    ERR_PUT_004(PUT_004, Messages.getString(PUT_004)),
    ERR_PUT_005(PUT_005, Messages.getString(PUT_005)),
    ERR_PUT_006(PUT_006, Messages.getString(PUT_006)),
    ERR_PUT_007(PUT_007, Messages.getString(PUT_007)),
    ERR_PUT_008(PUT_008, Messages.getString(PUT_008)),
    ERR_PUT_009(PUT_009, Messages.getString(PUT_009)),
    ERR_PUT_010(PUT_010, Messages.getString(PUT_010)),
    ERR_PUT_011(PUT_011, Messages.getString(PUT_011)),
    ERR_PUT_012(PUT_012, Messages.getString(PUT_012)),
    ERR_PUT_013(PUT_013, Messages.getString(PUT_013)), 
    ERR_PUT_014(PUT_014, Messages.getString(PUT_014)), 
    ERR_PUT_015(PUT_015, Messages.getString(PUT_015)),
    ERR_PUT_016(PUT_016, Messages.getString(PUT_016)),
    ERR_PUT_017(PUT_017, Messages.getString(PUT_017)),
    ERR_PUT_018(PUT_018, Messages.getString(PUT_018));
                  
    /** Error code. */
    private String code;
    
    /** Error text. */
    private String text;
    
    /**
     * Creates a new error entry with the given code and text.
     * @param cde Error code.
     * @param txt Message text. 
     */
    EnumErrorCatalog(final String cde, final String txt) {
        code = cde;
        text = txt;
    }
    
    /**
     * Gets the error text.
     * @return Error text. 
     */
    public String getMessage() {
        return text;
    }
    
    /** 
     * Gets the error code.
     * @return Error code.
     */
    public String getCode() {
        return code;
    }   
}


