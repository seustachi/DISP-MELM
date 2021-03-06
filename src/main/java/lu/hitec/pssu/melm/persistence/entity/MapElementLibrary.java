package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "map_element_library", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "major_version", "minor_version" }))
@SequenceGenerator(name = "map_element_library_seq", sequenceName = "map_element_library_seq")
public class MapElementLibrary {

  @Column(name = "icon_md5", nullable = false, updatable = true)
  private String iconMd5;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_library_seq")
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "major_version", nullable = false, updatable = true)
  private int majorVersion;

  @Column(name = "minor_version", nullable = false, updatable = true)
  private int minorVersion;

  @Column(name = "name", nullable = false, updatable = true)
  private String name;

  public MapElementLibrary() {
  }

  public MapElementLibrary(final String name, final int majorVersion, final int minorVersion, final String iconMd5) {
    this.name = name;
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.iconMd5 = iconMd5;
  }

  public String getIconMd5() {
    return iconMd5;
  }

  public String getIconPath() {
    // we store the file in a 2 levels folders hierarchy, taking the first and second char of the hash
    // so the typical path we be something like /b/e/bejdfgjdfgjdfgj.png
    return String.format("%s/%s/%s.png", iconMd5.substring(0, 1), iconMd5.substring(1, 2), iconMd5);
  }

  public long getId() {
    return id;
  }

  public int getMajorVersion() {
    return majorVersion;
  }

  public int getMinorVersion() {
    return minorVersion;
  }

  public String getName() {
    return name;
  }

  public void setIconMd5(final String iconMd5) {
    this.iconMd5 = iconMd5;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setMajorVersion(final int majorVersion) {
    this.majorVersion = majorVersion;
  }

  public void setMinorVersion(final int minorVersion) {
    this.minorVersion = minorVersion;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "MapElementLibrary [iconMd5=" + iconMd5 + ", id=" + id + ", majorVersion=" + majorVersion + ", minorVersion=" + minorVersion
        + ", name=" + name + "]";
  }

}
