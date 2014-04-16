package lu.hitec.pssu.melm.persistence.dao;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import lu.hitec.pssu.melm.persistence.entity.MapElementIcon;

public class MapElementIconDAOImpl implements MapElementIconDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<MapElementIcon> listAllIcons() {
		final TypedQuery<MapElementIcon> query = em.createQuery("SELECT mei FROM MapElementIcon mei ORDER BY mei.id", MapElementIcon.class);
		return query.getResultList();
	}

	@Override
	public void addMapElementIcon(final File icon) {
		final MapElementIcon mapElementIcon = new MapElementIcon();
		mapElementIcon.setPath(icon.getAbsolutePath());
		mapElementIcon.setPic100pxMd5(icon.getName());
		mapElementIcon.setSizeInBytes(icon.length());
		em.persist(mapElementIcon);

	}

	@Override
	public void delete(final long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exist(final String hash, final long size) {
		// TODO Auto-generated method stub
		return false;
	}

}
