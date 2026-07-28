package com.ug.smartcampus.service;

import com.ug.smartcampus.model.Request;import com.ug.smartcampus.datastructures.nonlinear.PriorityQueue;import java.util.*;
public class SchedulingService{private final PriorityQueue<Request> pending=new PriorityQueue<>(Comparator.comparingInt(Request::getPriority).reversed().thenComparing(Request::getRequestedTime));public void add(Request r){pending.offer(r);}public Request next(){return pending.dequeue();}public List<Request> plan(){List<Request>out=new ArrayList<>();while(!pending.isEmpty())out.add(pending.dequeue());return out;}public int pendingCount(){return pending.size();}}
