package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

public interface MapElementIconDAO {

  MapElementIcon addMapElementIcon(final String hash, final long size, final String displayName, final MapElementIconAnchor anchor);

  void delete(long id);

  void deleteMapElementIconForUnitTest(final String hash, final long size);

  boolean exist(String hash, long size);

  MapElementIcon getMapElementIcon(long id);

  MapElementIcon getMapElementIcon(final String hash, final long size);

  // FIXME this method should be move to MapElementLibraryIconDAO and takes the libraryId
  boolean iconsAvailable();

  List<MapElementIcon> listAllIcons();

  MapElementIcon updateMapElementIcon(final long id, final String hash, final long size, final String displayName,
      final MapElementIconAnchor anchor);

  MapElementIcon updateMapElementIcon(final long id, final String displayName, final MapElementIconAnchor anchor);

}
