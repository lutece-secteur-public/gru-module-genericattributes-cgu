/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.genericattributes.modules.cgu.service.entrytype;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.cgu.business.CguVersion;
import fr.paris.lutece.plugins.cgu.service.CguService;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.GenericAttributeError;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;

/**
 * 
 * Abstract entry type for cgu
 *
 */
public abstract class AbstractEntryTypeCgu extends EntryTypeService
{

    private static final String PARAMETER_IS_MINIMUM_AGE = "is_minimum_age";
    private static final String PARAMETER_IS_ACCEPTED_CGU = "is_accepted_cgu";
    private static final String PARAMETER_CGU_CODE = "cgu_code";

    private static final String FIELD_IS_MINIMUM_AGE = "is_minimum_age";
    private static final String FIELD_IS_ACCEPTED_CGU = "is_accepted_cgu";
    private static final String FIELD_MINIMUM_AGE = "minimum_age";
    private static final String FIELD_CGU_CODE = "cgu_code";
    private static final String FIELD_CGU_VERSION = "cgu_version";

    private static final String SUFFIX_UNDERSCORE = "_";

    private static final String MESSAGE_SEPARATOR = "<br/>";
    private static final String MESSAGE_IS_MINIMUM_AGE_REQUIRED = "module.genericattributes.cgu.message.mandatory.isMinimumAgeRequired";
    private static final String MESSAGE_ACCEPTED_CGU_REQUIRED = "module.genericattributes.cgu.message.mandatory.acceptedCGURequired";
    private static final String MESSAGE_INVALID_CGU_CODE = "module.genericattributes.cgu.messageInvalidCguCode";

    private static final int ZERO = 0;

    @Inject
    private CguService _cguService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestData( Entry entry, HttpServletRequest request, Locale locale )
    {
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strFieldError = StringUtils.EMPTY;

        if ( StringUtils.isBlank( strTitle ) )
        {
            strFieldError = FIELD_TITLE;
        }

        if ( StringUtils.isNotBlank( strFieldError ) )
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, locale ),
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        String strCode = request.getParameter( PARAMETER_ENTRY_CODE );
        String strCguCode = request.getParameter( PARAMETER_CGU_CODE );
        String strHelpMessage = ( request.getParameter( PARAMETER_HELP_MESSAGE ) != null ) ? request.getParameter( PARAMETER_HELP_MESSAGE ).trim( ) : null;
        String strCssClass = request.getParameter( PARAMETER_CSS_CLASS );

        entry.setTitle( strTitle );
        entry.setCSSClass( strCssClass );
        entry.setHelpMessage( strHelpMessage );
        entry.setCode( strCode );
        setValue( entry, FIELD_CGU_CODE, strCguCode );
        setValue( entry, FIELD_IS_ACCEPTED_CGU, Boolean.FALSE.toString( ) );

        CguVersion cguVersion = _cguService.findLastVersion( strCguCode );
        if ( cguVersion != null )
        {
            setValue( entry, FIELD_IS_MINIMUM_AGE, Boolean.FALSE.toString( ) );
            setValue( entry, FIELD_MINIMUM_AGE, String.valueOf( cguVersion.getMinimumAge( ) ) );
            setValue( entry, FIELD_CGU_VERSION, String.valueOf( cguVersion.getVersion( ) ) );
        }
        else
        {
            Object [ ] tabRequiredFields = {
                I18nService.getLocalizedString( strFieldError, locale ),
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_CGU_CODE, tabRequiredFields, AdminMessage.TYPE_ERROR );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericAttributeError getResponseData( Entry entry, HttpServletRequest request, List<Response> listResponse, Locale locale )
    {
        List<String> listErrorMessage = new ArrayList<>( );

        if ( Integer.parseInt( findFieldByCode( entry, FIELD_MINIMUM_AGE ).getValue( ) ) > ZERO
                && request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) + SUFFIX_UNDERSCORE + PARAMETER_IS_MINIMUM_AGE ) == null )
        {
            Response responseEntry = new Response( );
            responseEntry.setEntry( entry );
            listResponse.add( responseEntry );
            listErrorMessage.add( MESSAGE_IS_MINIMUM_AGE_REQUIRED );

        }
        if ( request.getParameter( PREFIX_ATTRIBUTE + entry.getIdEntry( ) + SUFFIX_UNDERSCORE + PARAMETER_IS_ACCEPTED_CGU ) == null )
        {
            Response responseEntry = new Response( );
            responseEntry.setEntry( entry );
            listResponse.add( responseEntry );

            listErrorMessage.add( MESSAGE_ACCEPTED_CGU_REQUIRED );
        }

        if ( !listErrorMessage.isEmpty( ) )
        {
            return createError( listErrorMessage, locale );
        }

        Field fieldIsMinimumAge = findFieldByCode( entry, FIELD_IS_MINIMUM_AGE );
        Response responseIsMinimumAge = new Response( );
        responseIsMinimumAge.setEntry( entry );
        responseIsMinimumAge.setResponseValue( Boolean.TRUE.toString( ) );
        responseIsMinimumAge.setField( fieldIsMinimumAge );

        listResponse.add( responseIsMinimumAge );

        Field fieldIsAccpetedCgu = findFieldByCode( entry, FIELD_IS_ACCEPTED_CGU );
        Response responseIsAcceptedCgu = new Response( );
        responseIsAcceptedCgu.setEntry( entry );
        responseIsAcceptedCgu.setResponseValue( Boolean.TRUE.toString( ) );
        responseIsAcceptedCgu.setField( fieldIsAccpetedCgu );

        listResponse.add( responseIsAcceptedCgu );

        Field fieldCodeCgu = findFieldByCode( entry, FIELD_CGU_CODE );
        Response responseCodeCgu = new Response( );
        responseCodeCgu.setEntry( entry );
        responseCodeCgu.setResponseValue( fieldCodeCgu.getValue( ) );
        responseCodeCgu.setField( fieldCodeCgu );

        listResponse.add( responseCodeCgu );

        Field fieldCguVersion = findFieldByCode( entry, FIELD_CGU_VERSION );
        Response responseCguVersion = new Response( );
        responseCguVersion.setEntry( entry );
        responseCguVersion.setResponseValue( fieldCguVersion.getValue( ) );
        responseCguVersion.setField( fieldCguVersion );

        listResponse.add( responseCguVersion );

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseValueForExport( Entry entry, HttpServletRequest request, Response response, Locale locale )
    {
        return response.getResponseValue( );
    }

    /**
     * Creates a Generic attribute error from the specified messages
     * 
     * @param listErrorMessage
     *            the error messages
     * @param locale
     *            the locale
     * @return the Generic attribute error
     */
    private GenericAttributeError createError( List<String> listErrorMessage, Locale locale )
    {
        GenericAttributeError error = new GenericAttributeError( );
        error.setMandatoryError( true );

        Iterator<String> iterator = listErrorMessage.iterator( );
        StringBuilder stringBuilder = new StringBuilder( );

        while ( iterator.hasNext( ) )
        {
            stringBuilder.append( I18nService.getLocalizedString( iterator.next( ), locale ) );

            if ( iterator.hasNext( ) )
            {
                stringBuilder.append( MESSAGE_SEPARATOR );
            }
        }

        error.setErrorMessage( stringBuilder.toString( ) );
        return error;
    }

    /**
     * Sets the specified value with the specified code in the specified entry
     * 
     * @param entry
     *            the entry
     * @param strCode
     *            the code
     * @param strValue
     *            the value
     */
    private void setValue( Entry entry, String strCode, String strValue )
    {
        List<Field> listField = entry.getFields( );

        if ( listField == null )
        {
            listField = new ArrayList<Field>( );
            entry.setFields( listField );
        }

        Field field = findFieldByCode( entry, strCode );

        if ( field == null )
        {
            field = new Field( );
            listField.add( field );
        }

        field.setCode( strCode );
        field.setValue( strValue );
    }

    /**
     * Finds a field with the specified code in the specified entry
     * 
     * @param entry
     *            the entry
     * @param strCode
     *            the code
     * @return the found field, or {@code null} otherwise
     */
    public static Field findFieldByCode( Entry entry, String strCode )
    {
        Field fieldFound = null;
        List<Field> listField = entry.getFields( );

        if ( listField != null )
        {
            for ( Field field : listField )
            {
                if ( strCode.equals( field.getCode( ) ) )
                {
                    fieldFound = field;
                    break;
                }
            }
        }

        return fieldFound;
    }

}
