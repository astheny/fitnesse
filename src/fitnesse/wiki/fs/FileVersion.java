package fitnesse.wiki.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fitnesse.wiki.VersionInfo;

/**
 * Represents a file under version control.
 */
public interface FileVersion {

  File getFile();
  InputStream getContent() throws IOException;
  VersionInfo getVersionInfo();
}
