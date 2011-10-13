package info.daviot.util.misc;

import java.util.Collection;
import java.util.LinkedList;


/**
 * Represents an ordered pair of objects which can be compared together.
 * 
 * @author MDA
 * @version NP
 */
public class OrderedPair
{
   private Comparable first;
   private Comparable second;

   /**
    * Tests if the first object is before the second one
    * using their natural order.
    * 
    * @return true if first &lt;= second or if at least one field is missing
    * @throws ClassCastException @see java.lang.Comparable#compareTo(Object)
    */
   public boolean isOrdered()
   {
   	  boolean oneIsNull= first==null || second == null;
   	  boolean oneIsEmpty= oneIsNull || "".equals(first.toString()) || "".equals(second.toString());
   	  boolean isOrdered=oneIsEmpty || getFirst().compareTo(getSecond()) <= 0;
   	  return isOrdered;
   }

   /**
    * Tests if the first object is strictly before the second one
    * using their natural order.
    * 
    * @return true if first &lt; second and both fields are present
    * @throws ClassCastException @see java.lang.Comparable#compareTo(Object)
    */
   public boolean isStrictlyOrdered()
   {
   	  return getFirst().compareTo(getSecond()) < 0;
   }

   public OrderedPair(Comparable first, Comparable second)
   {
   	  setFirst(first);
   	  setSecond(second);
   }

   public Comparable getFirst()
   {
      return first;
   }

   public void setFirst(Comparable first)
   {
      this.first = first;
   }
   public Comparable getSecond()
   {
      return second;
   }

   public void setSecond(Comparable second)
   {
      this.second = second;
   }
   
   public String toString()
   {
   	  return "("+first+","+second+")";
   }
   
   /**
    * Gets the values in the pair as a Collection.
    * 
    * @return a Collection of Comparable with 2 items
    */
   public Collection getValues()
   {
   	  Collection values=new LinkedList();
   	  values.add(getFirst());
   	  values.add(getSecond());
   	  return values;
   }
   
   public boolean equals(Object anObject)
   {
   	  try
   	  {
   	  	 OrderedPair otherPair=(OrderedPair) anObject;
   	  	 return otherPair.first.equals(first) && otherPair.second.equals(second);
   	  }
   	  catch (ClassCastException e)
   	  {
   	  	 return false;
   	  }
   }
}