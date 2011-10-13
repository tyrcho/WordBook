package info.daviot.exception;



/** 
 * Standard exception class for NP.
 * Can hold the following information 
 * (the 2 last parameters can be null) :
 * <ul>
 * <li>type</li>
 * <li>severity</li>
 * <li>module</li>
 * <li>team</li>
 * <li>comment</li>
 * <li>messageId</li>
 * <li>nestedException</li>
 * </ul>
 * 
 * @author MDA
 * @version NP
 */
public abstract class AbstractException extends SerializableExceptionWithNested
{
   private String type;
   private String severity;
   private String description;
   private String module;
   private String comment;
   private String messageId;
   protected static String lineSeparator = System.getProperty("line.separator");

   public static final String WARNING = "warning";
   public static final String ERROR = "error";
   public static final String FATAL = "fatal error";
   public static final String DEFAULT_SEVERITY = ERROR;
   
   /** 
    * Constructs an NpException linked to a message and a nested Exception.
    * 
    * @param type the type of the NpException, which should be obtained by a constant
    * @param description short description of the error, could be obtained by the parent error
    * @param severity the severity of the NpException, which should be obtained by a constant
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    * @param messageId the id of the message which caused the error
    * @param nestedException the nested exception which caused the error
    */
   public AbstractException(
      String type,
      String description,
      String severity,
      String module,
      String comment,
      String messageId,
      Throwable nestedException)
   {
      super(description, nestedException);
      this.type = type;
      this.description = description;
      this.severity = severity;
      this.module = module;
      this.comment = comment;
      this.messageId = messageId;
   }

   /** 
    * Constructs an NpException.
    * 
    * @param type the type of the NpException, which should be obtained by a constant
    * @param description short description of the error, could be obtained by the parent error
    * @param severity the severity of the NpException, which should be obtained by a constant
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    */
   public AbstractException(
      String type,
      String description,
      String severity,
      String module,
      String comment)
   {
      this(type, description, severity, module, comment, null, null);
   }
   
   /** 
    * Constructs an NpException.
    * 
    * @param type the type of the NpException, which should be obtained by a constant
    * @param description short description of the error, could be obtained by the parent error
    * @param severity the severity of the NpException, which should be obtained by a constant
    * @param module the calling module
    * @param comment the full description with the detailed procedure to investigate the error
    */
   public AbstractException(
      String type,
      String description,
      String severity,
      String module,
      String comment,
      String messageId)
   {
      this(type, description, severity, module, comment, messageId, null);
   }
   
   /**
    * Appends a comment to the current comments.
    * 
    * @param newComment the comment to add
    */
   public void addComment(String newComment)
   {
   	  comment=comment+lineSeparator+newComment;
   }
   
   public String toString()
   {
      return "Exception of type <"
         + type
         + "> and severity <"
         + severity
         + "> thrown by <"
         + module
         + "> "
         + (messageId != null ? (" when processing message with Id <" + messageId + ">") : "")
         + " : "
         + lineSeparator
         + lineSeparator
         + description
         + lineSeparator
         + lineSeparator
         + lineSeparator
         + "Additional comment : "
         + lineSeparator
         + comment
         + lineSeparator
         + lineSeparator
         + lineSeparator
         + "Nested exception is : "
         + getNestedExceptionStackString()
         + lineSeparator
;
   }

   /**
    * Get type, such as IO or database.
    *
    * @return type as String.
    */
   public String getType()
   {
      return type;
   }

   /**
    * Get severity (WARNING, ERROR or FATAL).
    *
    * @return severity as String.
    */
   public String getSeverity()
   {
      return severity;
   }

   /**
    * Get the module responsible for the error.
    *
    * @return module as String.
    */
   public String getModule()
   {
      return module;
   }


   /**
    * Get the full description.
    *
    * @return comment as String.
    */
   public String getComment()
   {
      return comment;
   }

   /**
    * Get messageId which identifies the message causing the error.
    *
    * @return messageId as String, or null if the error is not linked to a message.
    */
   public String getMessageId()
   {
      return messageId;
   }

   /**
    * Get the error description.
    *
    * @return description as String.
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * Set the error description.
    *
    * @param description the value to set.
    */
   public void setDescription(String description)
   {
      this.description = description;
   }

   /**
    * Sets the severity
    * @param severity The severity to set
    */
   public void setSeverity(String severity)
   {
      this.severity = severity;
   }
}