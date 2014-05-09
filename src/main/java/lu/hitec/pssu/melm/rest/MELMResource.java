package lu.hitec.pssu.melm.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.services.MELMService;
import lu.hitec.pssu.melm.utils.MELMUtils;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/rest")
@Component
@SuppressWarnings("static-method")
public class MELMResource {
  private static final String DEFAULT_MEDIA_TYPE = "image/png";

  private static final Logger LOGGER = LoggerFactory.getLogger(MELMResource.class);

  // Limit in bytes for file upload (overidden by services limits)
  private static final int MAX_FILE_SIZE = 8 * 1024 * 1024;

  @Autowired
  private MELMService melmService;

  @Context
  private HttpServletRequest request;

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/delete/{id}")
  public Response deleteIcon(@PathParam("id") final long id) throws MELMException {
    try {
      melmService.deleteIconAndFiles(id);
    } catch (final MELMException e) {
      LOGGER.warn("Error in deleteIcon", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListIcons();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/delete/{id}")
  public Response deleteLibrary(@PathParam("id") final long id) {
    melmService.deleteLibrary(id);
    return gotoListLibraries();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/delete/{name}/{majorVersion}/{minorVersion}/{libraryIconId}")
  public Response deleteLibraryIcon(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion, @PathParam("libraryIconId") final long libraryIconId) {
    assert name != null : "name is null";
    melmService.deleteLibraryIcon(libraryIconId);
    return getLibraryIcons(name, majorVersion, minorVersion);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/details/{id}")
  public Response getIconDetails(@PathParam("id") final long id) {
    return Response.ok(new Viewable("/iconDetails", melmService.getIcon(id))).build();
  }

  /**
   * It is better to use the id of icon and the size than the path of icon for security issues. Because someone could access file system.
   */
  @GET
  @Produces("image/*")
  @Path("/icons/file/{id}/{size}")
  public Response getIconFile(@PathParam("id") final long id, @PathParam("size") @Nonnull final String size) {
    assert size != null : "Size is null";
    final File file = melmService.getIconFile(id, size);
    if (!file.exists()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(file, DEFAULT_MEDIA_TYPE).build();
  }

  /**
   * Same comment as for getIconFile method.
   */
  @GET
  @Produces("image/*")
  @Path("/libraries/icon/file/{id}")
  public Response getLibraryIconFile(@PathParam("id") final long id) {
    final File file = melmService.getLibraryIconFile(id);
    if (!file.exists()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(file, DEFAULT_MEDIA_TYPE).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/{name}/{majorVersion}/{minorVersion}")
  public Response getLibraryIcons(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    final MapElementLibrary library = melmService.getLibrary(name, majorVersion, minorVersion);
    return Response.ok(
        new Viewable("/libraryIcons", new LibraryIconsModel(library, melmService.getLibraryIcons(name, majorVersion, minorVersion))))
        .build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/add")
  public Response gotoAddIcon() {
    return Response.ok(new Viewable("/addIcon")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/add")
  public Response gotoAddLibrary() {
    return Response.ok(new Viewable("/addLibrary")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/add/{name}/{majorVersion}/{minorVersion}")
  public Response gotoAddLibraryIcon(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    final MapElementLibrary library = melmService.getLibrary(name, majorVersion, minorVersion);
    return Response.ok(new Viewable("/addLibraryIcon", new AddLibraryIconsModel(library, melmService.listAllIcons()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response gotoImportLibrary() {
    return Response.ok(new Viewable("/importLibrary")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons")
  public Response gotoListIcons() {
    return Response.ok(new Viewable("/icons", new IconsModel(melmService.listAllIcons()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries")
  public Response gotoListLibraries() {
    return Response.ok(new Viewable("/libraries", new LibrariesModel(melmService.listAllLibraries()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response gotoStart() {
    return Response.ok(new Viewable("/start")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/update/{id}")
  public Response gotoUpdateLibrary(@PathParam("id") final long id) {
    final MapElementLibrary library = melmService.getLibrary(id);
    return Response.ok(new Viewable("/updateLibrary", library)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/update/{libraryIconId}")
  public Response gotoUpdateLibraryIcon(@PathParam("libraryIconId") final long libraryIconId) {
    final MapElementLibraryIcon libraryIcon = melmService.getLibraryIcon(libraryIconId);
    return Response.ok(new Viewable("/updateLibraryIcon", new UpdateLibraryIconsModel(libraryIcon, melmService.listAllIcons()))).build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/add")
  public Response performAddIcon() {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final IconUpload iconUpload = parseIconUpload();
      if (iconUpload.getLargeIconFile().length() == 0) {
        return Response.status(Status.BAD_REQUEST).entity("Icon file is invalid").build();
      }
      melmService.addIconAndFiles(iconUpload.getDisplayName(), iconUpload.getLargeIconFile());
    } catch (final Exception e) {
      LOGGER.warn("Error in performAddIcon", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    return gotoListIcons();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/add")
  public Response performAddLibrary() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryUpload libraryUpload = parseLibraryUpload();
      if (libraryUpload.getIconFile().length() == 0) {
        return Response.status(Status.BAD_REQUEST).entity("Icon file is invalid").build();
      }
      final String hashForFile = melmService.addLibraryIcon(libraryUpload.getIconFile());
      melmService.addLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(), hashForFile);
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/add")
  public Response performAddLibraryIcon() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    final LibraryIconUpload libraryIconUpload = parseLibraryIconUpload();
    try {
      if (libraryIconUpload == null) {
        final String msg = "Failed to parse parameters";
        LOGGER.warn(msg);
        return Response.status(Status.BAD_REQUEST).entity(msg).build();
      }
      melmService.addLibraryIcon(libraryIconUpload.getLibraryName(), Integer.parseInt(libraryIconUpload.getMajorVersion()),
          Integer.parseInt(libraryIconUpload.getMinorVersion()), Integer.parseInt(libraryIconUpload.getIconIndex()),
          libraryIconUpload.getIconName(), libraryIconUpload.getIconDescription(), Long.parseLong(libraryIconUpload.getIconId()));
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return getLibraryIcons(libraryIconUpload.getLibraryName(), Integer.parseInt(libraryIconUpload.getMajorVersion()),
        Integer.parseInt(libraryIconUpload.getMinorVersion()));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/hello/{value}")
  public Response performHelloWorld(@PathParam("value") final String value) throws MELMException {
    final String result = String.format("Hello %s", value);
    return Response.status(Response.Status.OK).entity(result).build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response performImportLibrary() {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryUpload libraryUpload = parseLibraryUpload();
      if (libraryUpload.getZipFile().length() == 0) {
        return Response.status(Status.BAD_REQUEST).entity("Zip file is invalid").build();
      }
      final File zipFile = melmService
          .importLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(), libraryUpload.getZipFile());
      final File libraryFolder = melmService.extractImportedLibrary(zipFile);
      if (libraryFolder != null) {
        final XMLSelectionPathParser libraryParser = melmService.validateAndParseImportedLibrary(libraryUpload.getLibraryName(),
            libraryUpload.getVersion());
        final String iconMd5 = melmService.moveImportedLibraryIcon(libraryParser, libraryFolder);
        final MapElementLibrary mapElementLibrary = melmService.addLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(),
            iconMd5);
        melmService.moveImportedIcons(mapElementLibrary, libraryParser, libraryFolder);
      }
    } catch (final MELMException e) {
      LOGGER.warn("Error in performImportLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/update")
  public Response performUpdateLibrary() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryUpload libraryUpload = parseLibraryUpload();
      if (libraryUpload.getIconFile().length() != 0) {
        final String hashForFile = melmService.addLibraryIcon(libraryUpload.getIconFile());
        melmService.updateLibrary(Long.parseLong(libraryUpload.getId()), libraryUpload.getLibraryName(), libraryUpload.getVersion(),
            hashForFile);
      } else {
        melmService.updateLibrary(Long.parseLong(libraryUpload.getId()), libraryUpload.getLibraryName(), libraryUpload.getVersion(), null);
      }
    } catch (final MELMException e) {
      LOGGER.warn("Error in performUpdateLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/update")
  public Response performUpdateLibraryIcon() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    final LibraryIconUpload libraryIconUpload = parseLibraryIconUpload();
    try {
      if (libraryIconUpload == null) {
        final String msg = "Failed to parse parameters";
        LOGGER.warn(msg);
        return Response.status(Status.BAD_REQUEST).entity(msg).build();
      }
      melmService.updateLibraryIcon(Long.parseLong(libraryIconUpload.getLibraryIconId()),
          Integer.parseInt(libraryIconUpload.getIconIndex()), libraryIconUpload.getIconName(), libraryIconUpload.getIconDescription(),
          Long.parseLong(libraryIconUpload.getIconId()));
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return getLibraryIcons(libraryIconUpload.getLibraryName(), Integer.parseInt(libraryIconUpload.getMajorVersion()),
        Integer.parseInt(libraryIconUpload.getMinorVersion()));
  }

  @GET
  @Produces("application/zip")
  @Path("/libraries/zip/{name}-{majorVersion}.{minorVersion}.zip")
  public Response performZipLibrary(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) throws MELMException {
    assert name != null : "name is null";
    final File zipFolder = melmService.prepareZipFile(name, majorVersion, minorVersion);
    final File zipFile = melmService.generateZipFile(zipFolder);
    return Response.ok(zipFile).build();
  }

  private IconUpload parseIconUpload() {
    File largeIconFile = null;
    String displayName = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(MAX_FILE_SIZE);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        final FileItemStream item = iter.next();
        stream = item.openStream();
        final String fieldName = item.getFieldName();
        if (Params.DISPLAY_NAME.equalsIgnoreCase(fieldName)) {
          displayName = Streams.asString(stream);
        } else if (Params.LARGE_FILE.equalsIgnoreCase(fieldName)) {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          largeIconFile = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(largeIconFile, IOUtils.toByteArray(stream));
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((displayName == null) || (largeIconFile == null)) {
      return null;
    }
    return new IconUpload(displayName, largeIconFile);
  }

  @CheckReturnValue
  private LibraryIconUpload parseLibraryIconUpload() {
    String name = null;
    String majorVersion = null;
    String minorVersion = null;
    String iconIndex = null;
    String iconName = null;
    String iconDescription = null;
    String iconId = null;
    String libraryIconId = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(MAX_FILE_SIZE);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        final FileItemStream item = iter.next();
        stream = item.openStream();
        final String fieldName = item.getFieldName();
        if (Params.NAME.equalsIgnoreCase(fieldName)) {
          name = Streams.asString(stream);
        } else if (Params.MAJOR_VERSION.equalsIgnoreCase(fieldName)) {
          majorVersion = Streams.asString(stream);
        } else if (Params.MINOR_VERSION.equalsIgnoreCase(fieldName)) {
          minorVersion = Streams.asString(stream);
        } else if (Params.ICON_INDEX.equalsIgnoreCase(fieldName)) {
          iconIndex = Streams.asString(stream);
        } else if (Params.ICON_NAME.equalsIgnoreCase(fieldName)) {
          iconName = Streams.asString(stream);
        } else if (Params.ICON_DESCRIPTION.equalsIgnoreCase(fieldName)) {
          iconDescription = Streams.asString(stream);
        } else if (Params.ICON_ID.equalsIgnoreCase(fieldName)) {
          iconId = Streams.asString(stream);
        } else if (Params.LIBRARY_ICON_ID.equalsIgnoreCase(fieldName)) {
          libraryIconId = Streams.asString(stream);
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((name == null) || (majorVersion == null) || (minorVersion == null) || (iconIndex == null) || (iconName == null)
        || (iconDescription == null) || (iconId == null)) {
      return null;
    }
    return new LibraryIconUpload(libraryIconId, name, majorVersion, minorVersion, iconIndex, iconName, iconDescription, iconId);
  }

  private LibraryUpload parseLibraryUpload() {
    String id = null;
    String name = null;
    String version = null;
    File libraryZipMaybeNull = null;
    File libraryIconMaybeNull = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(MAX_FILE_SIZE);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        final FileItemStream item = iter.next();
        stream = item.openStream();
        final String fieldName = item.getFieldName();
        if (Params.ID.equalsIgnoreCase(fieldName)) {
          id = Streams.asString(stream);
        } else if (Params.NAME.equalsIgnoreCase(fieldName)) {
          name = Streams.asString(stream);
        } else if (Params.VERSION.equalsIgnoreCase(fieldName)) {
          version = Streams.asString(stream);
        } else if (Params.FILE.equalsIgnoreCase(fieldName)) {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          libraryZipMaybeNull = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(libraryZipMaybeNull, IOUtils.toByteArray(stream));
        } else if (Params.FILE_ICON.equalsIgnoreCase(fieldName)) {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          libraryIconMaybeNull = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(libraryIconMaybeNull, IOUtils.toByteArray(stream));
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((name == null) || (version == null)) {
      return null;
    }
    return new LibraryUpload(id, name, version, libraryZipMaybeNull, libraryIconMaybeNull);
  }

  private final class IconUpload {
    private final String displayName;
    private final File largeIconFile;

    public IconUpload(final String displayName, final File largeIconFile) {
      this.displayName = displayName;
      this.largeIconFile = largeIconFile;
    }

    public String getDisplayName() {
      return displayName;
    }

    public File getLargeIconFile() {
      return largeIconFile;
    }
  }

  private final class LibraryIconUpload {
    private final String iconDescription;
    private final String iconId;
    private final String iconIndex;
    private final String iconName;
    private final String libraryIconId;
    private final String libraryName;
    private final String majorVersion;
    private final String minorVersion;

    public LibraryIconUpload(final String libraryIconId, final String libraryName, final String majorVersion, final String minorVersion,
        final String iconIndex, final String iconName, final String iconDescription, final String iconId) {
      this.libraryIconId = libraryIconId;
      this.libraryName = libraryName;
      this.majorVersion = majorVersion;
      this.minorVersion = minorVersion;
      this.iconIndex = iconIndex;
      this.iconName = iconName;
      this.iconDescription = iconDescription;
      this.iconId = iconId;
    }

    public String getIconDescription() {
      return iconDescription;
    }

    public String getIconId() {
      return iconId;
    }

    public String getIconIndex() {
      return iconIndex;
    }

    public String getIconName() {
      return iconName;
    }

    public String getLibraryIconId() {
      return libraryIconId;
    }

    public String getLibraryName() {
      return libraryName;
    }

    public String getMajorVersion() {
      return majorVersion;
    }

    public String getMinorVersion() {
      return minorVersion;
    }

  }

  private final class LibraryUpload {
    private final File iconFile;
    private final String id;
    private final String libraryName;
    private final String version;
    private final File zipFile;

    public LibraryUpload(final String id, final String libraryName, final String version, final File zipFile, final File iconFile) {
      this.id = id;
      this.libraryName = libraryName;
      this.version = version;
      this.zipFile = zipFile;
      this.iconFile = iconFile;
    }

    public File getIconFile() {
      return iconFile;
    }

    public String getId() {
      return id;
    }

    public String getLibraryName() {
      return libraryName;
    }

    public String getVersion() {
      return version;
    }

    public File getZipFile() {
      return zipFile;
    }
  }
}