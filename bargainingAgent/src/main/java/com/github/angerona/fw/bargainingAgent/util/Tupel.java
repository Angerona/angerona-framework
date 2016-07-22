package com.github.angerona.fw.bargainingAgent.util;

public class Tupel<T, U>
{
   T first;
   U second;

   public Tupel(T a, U b)
   {
	first = a;
    second = b;
   }

   public T getFirst(){ return first;}
   public U getLast(){ return second;}
   
   public String toString(){
	   return "(" + first + "," + second + ")";
   }
}
