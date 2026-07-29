package com.ug.smartcampus.service;

import com.ug.smartcampus.algorithm.search.BinarySearch;import java.util.*;
public class SearchService{public int findRequestId(int[]sortedIds,int id){return BinarySearch.search(sortedIds,id);}public<T>List<T> filter(Collection<T>items,java.util.function.Predicate<T>predicate){return items.stream().filter(predicate).toList();}}
