package lu.hitec.pssu.melm.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;
import lu.hitec.pssu.melm.utils.MapElementIconAnchor;

import org.springframework.transaction.annotation.Transactional;

public class MapElementIconDAOImpl implements MapElementIconDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public MapElementIcon addMapElementIcon(final String hash, final long size, final String displayName, final MapElementIconAnchor anchor) {
		final MapElementIcon mapElementIcon = new MapElementIcon();
		mapElementIcon.setPic100pxMd5(hash);
		mapElementIcon.setSizeInBytes(size);
		mapElementIcon.setDisplayName(displayName);
		mapElementIcon.setAnchor(anchor);
		em.persist(mapElementIcon);
		return mapElementIcon;
	}

	@Override
	@Transactional
	public void delete(final long id) {
		final MapElementIcon icon = em.find(MapElementIcon.class, id);
		em.remove(icon);
	}

	@Override
	@Transactional
	public void deleteMapElementIconForUnitTest(final String hash, final long size) {
		final Query query = em.createQuery("DELETE FROM MapElementIcon mei WHERE mei.pic100pxMd5 = :hash AND sizeInBytes = :size");
		query.setParameter("hash", hash);
		query.setParameter("size", size);
		query.executeUpdate();
	}

	@Override
	public boolean exist(final String hash, final long size) {
		final TypedQuery<MapElementIcon> query = em.createQuery("SELECT mei FROM MapElementIcon mei WHERE mei.pic100pxMd5 = :hash AND sizeInBytes = :size", MapElementIcon.class)
				.setParameter("hash", hash).setParameter("size", size);

		return query.getResultList().size() > 0;
	}

	@Override
	public MapElementIcon getMapElementIcon(final long id) {
		return em.find(MapElementIcon.class, id);

	}

	@Override
	public MapElementIcon getMapElementIcon(final String hash, final long size) {
		final TypedQuery<MapElementIcon> query = em.createQuery("SELECT mei FROM MapElementIcon mei WHERE mei.pic100pxMd5 = :hash AND sizeInBytes = :size", MapElementIcon.class);
		query.setParameter("hash", hash);
		query.setParameter("size", size);
		return query.getSingleResult();
	}

	@Override
	public List<MapElementIcon> listAllIcons() {
		final TypedQuery<MapElementIcon> query = em.createQuery("SELECT mei FROM MapElementIcon mei ORDER BY mei.id", MapElementIcon.class);
		return query.getResultList();
	}

	@Override
	public boolean iconsAvailable() {
		final TypedQuery<MapElementIcon> query = em.createQuery("SELECT mei FROM MapElementIcon mei WHERE mei NOT IN (SELECT DISTINCT meli.icon from MapElementLibraryIcon meli)", MapElementIcon.class);
		final List<MapElementIcon> resultList = query.getResultList();
		return (null != resultList) && (0 < resultList.size());
	}
}
