/*
 * Copyright (C) 2011 The Libphonenumber Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Loïc Corbasson
 */

package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberToTimeZonesMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A command that tranforms strings representing phone numbers to results from parsing, validating 
 * and formatting the numbers. A default country, representing the country that we are expecting the
 * number to be from, can be specified as a two-letter ISO 3166-2 country code.
 */
public class PhoneNumberCLI {
  public static void main(String[] args) {
  
    String _defaultCountryCode = "US";
    String _languageCode = "en";  // Default languageCode to English if nothing is entered.
    String defaultCountryCode = _defaultCountryCode;
    String languageCode = _languageCode;
  
    Options options = new Options();
    options.addOption("h", "help", false, "Show help.");
    options.addOption("c", "country", true, "Set the default country for phone numbers lacking an "
        + "international prefix, as a two-letter ISO 3166-2 country code.");
    options.addOption("l", "lang", true, "Set the language to use for the display, as an IETF "
        + "language tag.");
  
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
  
      if (cmd.hasOption("c")) {
        defaultCountryCode = cmd.getOptionValue("c");
      }
      if (cmd.hasOption("l")) {
        languageCode = cmd.getOptionValue("l");
      }
  
      PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
  
      List<String> phoneNumbers = cmd.getArgList();
  
      for (String phoneNumber : phoneNumbers) {
        try {
          PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumber, defaultCountryCode);;
       
          boolean isValid = phoneUtil.isValidNumber(phoneNumberProto);
    
          if ((phoneUtil.getRegionCodeForNumber(phoneNumberProto) == defaultCountryCode) 
              && (phoneUtil.getRegionCodeForNumber(phoneNumberProto) != null)) {
            System.out.println(phoneUtil.format(phoneNumberProto, PhoneNumberFormat.NATIONAL));
          } else {
            System.out.println(phoneUtil.format(phoneNumberProto, PhoneNumberFormat.INTERNATIONAL));
          }
        } catch (NumberParseException e) {
          System.err.println("NumberParseException was thrown: " + e.toString());
        }
      }
  
    } catch (ParseException e) {
      System.err.println("ParseException was thrown: " + e.toString());
    }
  
  }
}
