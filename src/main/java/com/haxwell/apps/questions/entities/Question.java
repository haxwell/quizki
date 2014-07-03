package com.haxwell.apps.questions.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.haxwell.apps.questions.constants.EntityStatusConstants;
import com.haxwell.apps.questions.interfaces.IQuestion;
import com.haxwell.apps.questions.utils.StringUtil;


/**
 * The persistent class for the QUESTION database table.
 * 
 */
@Entity
@Table(name="question")
public class Question extends AbstractEntity implements IQuestion, EntityWithAnIntegerIDBehavior, EntityWithADifficultyObjectBehavior, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String description;
	private String text;
	
	@Column(name="ENTITY_STATUS")
	private long entityStatus = EntityStatusConstants.ACTIVATED;	

	//uni-directional many-to-one association to Difficulty
    @ManyToOne()
	private Difficulty difficulty;

	//uni-directional many-to-one association to QuestionType
    @ManyToOne()
	@JoinColumn(name="type_id")
	private QuestionType questionType;

	//bi-directional many-to-one association to User
    @ManyToOne()
	private User user;

	//uni-directional many-to-many association to Choice
    @ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(
		name="question_choice"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="choice_id")
			}
		)
	private Set<Choice> choices;

	//uni-directional many-to-many association to Topic
    @ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(
		name="question_topic"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="topic_id")
			}
		)
	private Set<Topic> topics;

	//uni-directional many-to-many association to Reference
    @ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(
		name="question_reference"
		, joinColumns={
			@JoinColumn(name="question_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="reference_id")
			}
		)
	private Set<Reference> references;

    public final static long DEFAULT_ID = -1;
    public final static String DEFAULT_TEXT = "";
    
    public Question() {
    	id = DEFAULT_ID;
    	text = DEFAULT_TEXT;
    }

    @Override
    public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	// TODO: rename this.. its removing the dynamic field markers, as well as HTML.
	public String getTextWithoutHTML() {
		String str = this.text.replaceAll("\\<.*?>","");
		
		str = str.replace("[[", "");
		str = str.replace("]]", "");
		
		return str;
	}
	
	@Override
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Difficulty getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public QuestionType getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	
	public long getEntityStatus() {
		return this.entityStatus;
	}
	
	public void setEntityStatus(long es) {
		this.entityStatus = es;
	}
	
	@Override
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Set<Choice> getChoices() {
		if (this.choices == null)
			this.choices = new HashSet<Choice>();
		
		return this.choices;
	}

	public void setChoices(Set<Choice> choices) {
		this.choices = choices;
	}
	
	public Set<Reference> getReferences() {
		if (this.references == null)
			this.references = new HashSet<Reference>();
		
		return this.references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}

	public Set<Topic> getTopics() {
		if (this.topics == null)
			this.topics = new HashSet<Topic>();
		
		return this.topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	@Override
	public String getEntityDescription() {
		return "question";
	}
	
	@Override
	public String toJSON() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getJSONOpening());
		sb.append(getJSON("id", getId() + "", APPEND_COMMA));
		sb.append(getJSON("description", getDescription(), APPEND_COMMA));
		sb.append(getJSON("text", getText(), APPEND_COMMA));
		sb.append(getJSON("textWithoutHTML", getTextWithoutHTML(), APPEND_COMMA));
		Difficulty diff = getDifficulty();
		sb.append(getJSON("difficulty_id", diff.getId()+"", APPEND_COMMA));
		sb.append(getJSON("difficulty_text", diff.getText(), APPEND_COMMA));
		sb.append(getJSON("type_id", getQuestionType().getId()+"", APPEND_COMMA));
		sb.append(getJSON("type_text", getQuestionType().getText(), APPEND_COMMA));

		sb.append(getJSON("choices", choices.iterator(), APPEND_COMMA));
		sb.append(getJSON("topics", topics.iterator(), APPEND_COMMA));
		sb.append(getJSON("references", references.iterator(), APPEND_COMMA));
		sb.append(getJSON("entityStatus", getEntityStatus() + "", APPEND_COMMA));

		String str = getDynamicDataAsJSON();
		sb.append(str);
		if (str.length() > 0) sb.append(",");
		
		sb.append(getJSON("user_id", getUser().getId()+"", APPEND_COMMA));
		sb.append(getJSON("user_name", getUser().getUsername()));
		
		sb.append(getJSONClosing());
		
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return this.text.hashCode() + (int)this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean rtn = (this == o);
		
		if (!rtn && o instanceof Question) {
			Question that = (Question)o;
			
			rtn = (this.id == that.id && StringUtil.equals(this.text, that.text)); 
		}
		
		return rtn;
	}
	
	@Override
	public int compareTo(Object o) {
		int rtn = 0;
		
		if (o instanceof Question){
			Question that = (Question)o;
			
			if (this.id > that.id)
				return -1;
			else if (this.id < that.id)
				return 1;
			
			rtn = this.text.compareTo(that.text);
		}
		
		return rtn;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("ID: " + getId());
		sb.append("  |Text: " + getText());
		
		return sb.toString();
	}
}