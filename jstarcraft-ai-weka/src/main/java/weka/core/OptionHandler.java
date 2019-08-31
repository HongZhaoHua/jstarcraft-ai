/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    OptionHandler.java
 *    Copyright (C) 1999-2012 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.core;

import java.util.Enumeration;

/** 
 * Interface to something that understands options.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version $Revision$
 */
public interface OptionHandler {

  /**
   * Returns an enumeration of all the available options..
   *
   * @return an enumeration of all available options.
   */
  Enumeration<Option> listOptions();

  /**
   * Sets the OptionHandler's options using the given list. All options
   * will be set (or reset) during this call (i.e. incremental setting
   * of options is not possible).
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  //@ requires options != null;
  //@ requires \nonnullelements(options);
  void setOptions(String[] options) throws Exception;

  /**
   * Gets the current option settings for the OptionHandler.
   *
   * @return the list of current option settings as an array of strings
   */
  //@ ensures \result != null;
  //@ ensures \nonnullelements(\result);
  /*@pure@*/ String[] getOptions();

  /**
   * Creates an instance of the class that the given option handler belongs to and sets the options for this
   * new instance by taking the option settings from the given option handler.
   *
   * If an exception is thrown when this process is performed, the fall back is to take a standard deep copy
   * of the given option handler object. If that also fails, an exception is thrown by this method.
   *
   * A message will be printed to the standard error if the object is deep copied. A stack trace is also output
   * in this case.
   *
   * @param toCopy the option handler to copy
   *
   * @exception Exception if the object could not be deep copied either
   */
  static OptionHandler makeCopy(OptionHandler toCopy) throws Exception {

    try {
      return (OptionHandler)Utils.forName(Object.class, toCopy.getClass().getCanonicalName(), toCopy.getOptions());
    } catch (Exception ex) {
      System.err.println("WARNING: failed to copy option handler " + toCopy.getClass().getCanonicalName() + " with " +
              "options " + String.join(" ", toCopy.getOptions()) + " --- trying deep copy instead");
      ex.printStackTrace();
      return (OptionHandler) (new SerializedObject(toCopy)).getObject();
    }
  }
}








