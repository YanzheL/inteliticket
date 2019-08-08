package org.yanzhe.inteliticket.utils;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExampleFileFilter extends FileFilter {

  @NotNull
  private static String TYPE_UNKNOWN = "Type Unknown";
  @NotNull
  private static String HIDDEN_FILE = "Hidden File";

  @Nullable
  private Hashtable filters = null;
  @Nullable
  private String description = null;
  @Nullable
  private String fullDescription = null;
  private boolean useExtensionsInDescription = true;

  /**
   * Creates a file filter. If no filters are added, then all files are accepted.
   *
   * @see #addExtension
   */
  public ExampleFileFilter() {
    this.filters = new Hashtable();
  }

  /**
   * Creates a file filter that accepts files with the given extension. Example: new
   * ExampleFileFilter("jpg");
   *
   * @see #addExtension
   */
  public ExampleFileFilter(String extension) {
    this(extension, null);
  }

  /**
   * Creates a file filter that accepts the given file type. Example: new ExampleFileFilter("jpg",
   * "JPEG Image Images");
   *
   * <p>Note that the "." before the extension is not needed. If provided, it will be ignored.
   *
   * @see #addExtension
   */
  public ExampleFileFilter(@Nullable String extension, @Nullable String description) {
    this();
    if (extension != null) {
      addExtension(extension);
    }
    if (description != null) {
      setDescription(description);
    }
  }

  /**
   * Creates a file filter from the given string array. Example: new ExampleFileFilter(String
   * {"gif", "jpg"});
   *
   * <p>Note that the "." before the extension is not needed adn will be ignored.
   *
   * @see #addExtension
   */
  public ExampleFileFilter(@NotNull String[] filters) {
    this(filters, null);
  }

  /**
   * Creates a file filter from the given string array and description. Example: new
   * ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
   *
   * <p>Note that the "." before the extension is not needed and will be ignored.
   *
   * @see #addExtension
   */
  public ExampleFileFilter(@NotNull String[] filters, @Nullable String description) {
    this();
    for (int i = 0; i < filters.length; i++) {
      // add filters one by one
      addExtension(filters[i]);
    }
    if (description != null) {
      setDescription(description);
    }
  }

  /**
   * Return true if this file should be shown in the directory pane, false if it shouldn't.
   *
   * <p>Files that begin with "." are ignored.
   *
   * @see #getExtension
   * @see FileFilter#accept
   */
  public boolean accept(@Nullable File f) {
    if (f != null) {
      if (f.isDirectory()) {
        return true;
      }
      String extension = getExtension(f);
      return extension != null && filters.get(getExtension(f)) != null;
    }
    return false;
  }

  /**
   * Return the extension portion of the file's name .
   *
   * @see #getExtension
   * @see FileFilter#accept
   */
  @Nullable
  public String getExtension(@Nullable File f) {
    if (f != null) {
      String filename = f.getName();
      int i = filename.lastIndexOf('.');
      if (i > 0 && i < filename.length() - 1) {
        return filename.substring(i + 1).toLowerCase();
      }
    }
    return null;
  }

  /**
   * Adds a filetype "dot" extension to filter against.
   *
   * <p>For example: the following code will create a filter that filters out all files except
   * those that end in ".jpg" and ".tif":
   *
   * <p>ExampleFileFilter filter = new ExampleFileFilter(); filter.addExtension("jpg");
   * filter.addExtension("tif");
   *
   * <p>Note that the "." before the extension is not needed and will be ignored.
   */
  public void addExtension(@NotNull String extension) {
    if (filters == null) {
      filters = new Hashtable(5);
    }
    filters.put(extension.toLowerCase(), this);
    fullDescription = null;
  }

  /**
   * Returns the human readable description of this filter. For example: "JPEG and GIF Image Files
   * (*.jpg, *.gif)"
   *
   * @see this::setDescription
   * @see this::setExtensionListInDescription
   * @see this::isExtensionListInDescription
   * @see FileFilter#getDescription
   */
  @Nullable
  public String getDescription() {
    if (fullDescription == null) {
      if (description == null || isExtensionListInDescription()) {
        fullDescription = description == null ? "(" : description + " (";
        // build the description from the extension list
        Enumeration extensions = filters.keys();
        if (extensions != null) {
          fullDescription += "." + extensions.nextElement();
          while (extensions.hasMoreElements()) {
            fullDescription += ", ." + extensions.nextElement();
          }
        }
        fullDescription += ")";
      } else {
        fullDescription = description;
      }
    }
    return fullDescription;
  }

  /**
   * Sets the human readable description of this filter. For example: filter.setDescription("Gif and
   * JPG Images");
   *
   * @see this::setDescription
   * @see this::setExtensionListInDescription
   * @see this::isExtensionListInDescription
   */
  public void setDescription(String description) {
    this.description = description;
    fullDescription = null;
  }

  /**
   * Returns whether the extension list (.jpg, .gif, etc) should show up in the human readable
   * description.
   *
   * <p>Only relevent if a description was provided in the constructor or using setDescription();
   *
   * @see this::getDescription
   * @see this::setDescription
   * @see this::setExtensionListInDescription
   */
  public boolean isExtensionListInDescription() {
    return useExtensionsInDescription;
  }

  /**
   * Determines whether the extension list (.jpg, .gif, etc) should show up in the human readable
   * description.
   *
   * <p>Only relevent if a description was provided in the constructor or using setDescription();
   *
   * @see this::getDescription
   * @see this::setDescription
   * @see this::isExtensionListInDescription
   */
  public void setExtensionListInDescription(boolean b) {
    useExtensionsInDescription = b;
    fullDescription = null;
  }
}
