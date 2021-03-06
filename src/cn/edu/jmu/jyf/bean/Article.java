package cn.edu.jmu.jyf.bean;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Article entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "article", catalog = "exchangeplatform")
public class Article implements java.io.Serializable {

	// Fields

	private Integer articleId;
	private User user;
	private String title;
	private String content;
	private Timestamp uploadDateTime;
	private Boolean isHidden;
	private String image;
	private Integer readNumber;
	private Set<Keyword> keywords = new HashSet<Keyword>(0);
	private Set<Like> likes = new HashSet<Like>(0);
	private Set<Bookmark> bookmarks = new HashSet<Bookmark>(0);

	// private Integer weight;

	// Constructors

	/** default constructor */
	public Article() {
	}

	/** minimal constructor */
	public Article(User user, String title, String content,
			Timestamp uploadDateTime, Boolean isHidden) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.uploadDateTime = uploadDateTime;
		this.isHidden = isHidden;
	}

	/** full constructor */
	public Article(User user, String title, String content,
			Timestamp uploadDateTime, Boolean isHidden, String image,
			Integer read, Set<Keyword> keywords, Set<Like> likes,
			Set<Bookmark> bookmarks) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.uploadDateTime = uploadDateTime;
		this.isHidden = isHidden;
		this.image = image;
		this.readNumber = read;
		this.keywords = keywords;
		this.likes = likes;
		this.bookmarks = bookmarks;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "articleId", unique = true, nullable = false)
	public Integer getArticleId() {
		return this.articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authorId", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "title", nullable = false, length = 45)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content", nullable = false, length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "uploadDateTime", nullable = false, length = 19)
	public Timestamp getUploadDateTime() {
		return this.uploadDateTime;
	}

	public void setUploadDateTime(Timestamp uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}

	@Column(name = "isHidden", nullable = false)
	public Boolean getIsHidden() {
		return this.isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	@Column(name = "image", length = 45)
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "readNumber")
	public Integer getReadNumber() {
		return this.readNumber;
	}

	public void setReadNumber(Integer readNumber) {
		this.readNumber = readNumber;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "article")
	public Set<Keyword> getKeywords() {
		return this.keywords;
	}

	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "article")
	public Set<Like> getLikes() {
		return this.likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "article")
	public Set<Bookmark> getBookmarks() {
		return this.bookmarks;
	}

	public void setBookmarks(Set<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	/*
	 * 计算文章权重
	 * 
	 * public Integer getWeight() { int bookmarkNumber = 0; int likeNumber = 0;
	 * if (this.bookmarks != null) { bookmarkNumber = this.bookmarks.size(); }
	 * if (this.likes != null) { likeNumber = this.likes.size(); } this.weight =
	 * bookmarkNumber * Config.WEIGHT_OF_BOOKMARK_IN_ARTICLE + likeNumber *
	 * Config.WEIGHT_OF_LIKE_IN_ARTICLE + readNumber
	 * Config.WEIGHT_OF_READNUMBER_IN_ARTICLE; return weight; }
	 * 
	 * public void setWeght(Integer weight) { this.weight = weight; }
	 */

}