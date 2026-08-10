package com.ug.smartcampus.service;

import com.ug.smartcampus.algorithm.allocation.GreedyResourceAllocation;import com.ug.smartcampus.model.*;import java.util.*;
public class ResourceAllocationService{public Map<Request,Resource> allocate(List<Request>requests,List<Resource>resources){return GreedyResourceAllocation.allocate(requests,resources);}}
