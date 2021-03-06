package cn.edu.jmu.jyf.dao;

import static org.hibernate.criterion.Example.create;

import java.sql.Date;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.jmu.jyf.bean.Article;
import cn.edu.jmu.jyf.config.Config;

/**
 * A data access object (DAO) providing persistence and search support for
 * Article entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see cn.edu.jmu.jyf.bean.Article
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ArticleDAO {
	private static final Logger log = LoggerFactory.getLogger(ArticleDAO.class);
	// property constants
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String IS_HIDDEN = "isHidden";
	public static final String IMAGE = "image";

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Article transientInstance) {
		log.debug("saving Article instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Article persistentInstance) {
		log.debug("deleting Article instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Article findById(java.lang.Integer id) {
		log.debug("getting Article instance with id: " + id);
		try {
			Article instance = (Article) getCurrentSession().get(
					"cn.edu.jmu.jyf.bean.Article", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Article> findByExample(Article instance) {
		log.debug("finding Article instance by example");
		try {
			List<Article> results = (List<Article>) getCurrentSession()
					.createCriteria("cn.edu.jmu.jyf.bean.Article")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Article instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Article as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Article> findByTitle(Object title) {
		return findByProperty(TITLE, title);
	}

	public List<Article> findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List<Article> findByIsHidden(Object isHidden) {
		return findByProperty(IS_HIDDEN, isHidden);
	}

	public List<Article> findByImage(Object image) {
		return findByProperty(IMAGE, image);
	}

	public List findAll() {
		log.debug("finding all Article instances");
		try {
			String queryString = "from Article";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Article merge(Article detachedInstance) {
		log.debug("merging Article instance");
		try {
			Article result = (Article) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Article instance) {
		log.debug("attaching dirty Article instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Article instance) {
		log.debug("attaching clean Article instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ArticleDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ArticleDAO) ctx.getBean("ArticleDAO");
	}

	public List getNew(Integer amount) {
		try {
			String queryString = "from Article as article where article.isHidden=0 order by article.uploadDateTime desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setFirstResult(0);
			queryObject.setMaxResults(amount);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get new failed", re);
			throw re;
		}
	}

	public List getHot(Integer amount, Integer begin) {
		try {
			String queryString = "from Article as article where article.isHidden=0 order by"
					+ " ((article.likes.size*?+article.bookmarks.size*?+"
					+ "article.readNumber*?)*TO_DAYS(article.uploadDateTime)*?) desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setInteger(0, Config.WEIGHT_OF_LIKE_IN_ARTICLE);
			queryObject.setInteger(1, Config.WEIGHT_OF_BOOKMARK_IN_ARTICLE);
			queryObject.setInteger(2, Config.WEIGHT_OF_READNUMBER_IN_ARTICLE);
			queryObject.setFloat(3, Config.WEIGHT_OF_TIME_IN_HOT);
			queryObject.setFirstResult(begin - 1);
			queryObject.setMaxResults(amount);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get hot failed", re);
			throw re;
		}
	}

	public List getByQuality(Integer amount, Integer begin) {
		try {
			String queryString = "from Article as article where article.isHidden=0 order by"
					+ " (article.likes.size*?+article.bookmarks.size*?+"
					+ "article.readNumber*?) desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setInteger(0, Config.WEIGHT_OF_LIKE_IN_ARTICLE);
			queryObject.setInteger(1, Config.WEIGHT_OF_BOOKMARK_IN_ARTICLE);
			queryObject.setInteger(2, Config.WEIGHT_OF_READNUMBER_IN_ARTICLE);
			queryObject.setFirstResult(begin - 1);
			queryObject.setMaxResults(amount);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get hot failed", re);
			throw re;
		}
	}

	public List getPastArticleList(Long time, Integer amount) {
		try {
			String queryString = "from Article as a where a.isHidden=0 and a.uploadDateTime<? order by a.uploadDateTime desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setTimestamp(0, new Date(time));
			queryObject.setMaxResults(amount);
			return queryObject.list();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

}