// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long requestDuration = request.getDuration();
    Collection<String> requestAttendees = request.getAttendees();
    ArrayList<TimeRange> validTimes = new ArrayList<TimeRange>();
    ArrayList<TimeRange> noOverlap = new ArrayList<TimeRange>();  
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();  
    int newStart;
    int newEnd;
    //edge case: if there are no attendees the whole day is returned        
    if (requestAttendees.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    //edge case: if the requested meeting is longer than a day there are no options
    if (requestDuration >= TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();  
    }
    //add the times for all meetings where there are attendees
    for (Event e: events) {
      if (!e.getAttendees().isEmpty()) {
        validTimes.add(e.getWhen());
      }
    }
    //if there are no meetings with attendees the whole day is returned 
    if(validTimes.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);    
    }  
    //if there are multiple meetings consolidate overlapping meetings into one range of time
    if (validTimes.size() > 1){
      for (int i = 1; i < validTimes.size(); i++){
        if (validTimes.get(i - 1).overlaps(validTimes.get(i))){
          newStart = Math.min(validTimes.get(i - 1).start(), validTimes.get(i).start());
          newEnd = Math.max(validTimes.get(i).end(), validTimes.get(i - 1).end());
          noOverlap.add(TimeRange.fromStartEnd(newStart, newEnd, false));
        } else {
          noOverlap.add(validTimes.get(i - 1));
          noOverlap.add(validTimes.get(i));
        }
      }
    } else {
      noOverlap = validTimes;
    }
    //if there is adequate time between the beginning of the day and the beginning of the first meeting add that time range to the output list   
    if(noOverlap.get(0).start() - TimeRange.START_OF_DAY >= requestDuration) {
      newStart = TimeRange.START_OF_DAY;
      newEnd = noOverlap.get(0).start();
      availableTimes.add(TimeRange.fromStartEnd(newStart, newEnd, false));    
    }
    //if there are adequate time between meetings add those time ranges to the output list
    for (int i = 0; i < noOverlap.size() - 1; i++) {
      if(noOverlap.get(i + 1).start() - noOverlap.get(i).end() >= requestDuration){
          newStart = noOverlap.get(i).end();
          newEnd = noOverlap.get(i + 1).start();
          availableTimes.add(TimeRange.fromStartEnd(newStart, newEnd, false));
      }
    }
    //if there is adequate time between the end of the day and the end of the last meeting add that time range to the output list   
    if(TimeRange.END_OF_DAY - noOverlap.get(noOverlap.size() - 1).end() >= requestDuration) {
        newStart = noOverlap.get(noOverlap.size() - 1).end();
        newEnd = TimeRange.END_OF_DAY;
      availableTimes.add(TimeRange.fromStartEnd(newStart,newEnd , true));
    }
    //return the list of available time ranges
    return availableTimes;
  }
}