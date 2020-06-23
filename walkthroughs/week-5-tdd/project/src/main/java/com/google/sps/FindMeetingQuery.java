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
    // edge case: if there are no attendees the whole day is returned  
    Collection<String> requestAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();      
    if (requestAttendees.isEmpty() && optionalAttendees.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    // edge case: if the requested meeting is longer than a day there are no options
    long requestDuration = request.getDuration();
    if (requestDuration >= TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();  
    }
    ArrayList<TimeRange> alreadyBookedTimes = getAlreadyBookedTimes(events, request);
    ArrayList<TimeRange> alreadyBookedTimesWOptional = getAlreadyBookedTimesWithOptional(events, request, alreadyBookedTimes);
    // if there are no meetings the whole day is returned 
    if (alreadyBookedTimes.isEmpty() && alreadyBookedTimesWOptional.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);    
    } 
    ArrayList<TimeRange> noOverlap = removeOverlap(alreadyBookedTimes);
    ArrayList<TimeRange> noOverlapWOptional = removeOverlap(alreadyBookedTimesWOptional);
    ArrayList<TimeRange> availableTimesWOptional = addAvailableRanges(request, noOverlapWOptional);
    ArrayList<TimeRange> availableTimes = addAvailableRanges(request, noOverlap);
    // return the list of available time ranges
    if (!availableTimesWOptional.isEmpty()) {
      return availableTimesWOptional;
    }
    return availableTimes;
  }
  
  // if there are multiple meetings consolidate overlapping meetings into one range of time
  public ArrayList<TimeRange> removeOverlap(ArrayList<TimeRange> times) {
    ArrayList<TimeRange> noOverlap = new ArrayList<TimeRange>();
    Collections.sort(times, TimeRange.ORDER_BY_START);
    int newStart;
    int newEnd;
    if (times.size() > 1) {
      for (int i = 1; i < times.size(); i++) {
        if (times.get(i - 1).overlaps(times.get(i))) {
          newStart = Math.min(times.get(i - 1).start(), times.get(i).start());
          newEnd = Math.max(times.get(i).end(), times.get(i - 1).end());
          if (!noOverlap.contains(TimeRange.fromStartEnd(newStart, newEnd, false))) {
            noOverlap.add(TimeRange.fromStartEnd(newStart, newEnd, false));
          }
        } else {
          noOverlap.add(times.get(i - 1));
          noOverlap.add(times.get(i));
        } 
      }      
    } else {
      return times;
    }
    return noOverlap;
  }

  // add the times for all meetings where there are mandatory attendees
  public ArrayList<TimeRange> getAlreadyBookedTimes(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> alreadyBookedTimes = new ArrayList<TimeRange>();
    Collection<String> requestAttendees = request.getAttendees();   
    for (Event event: events) {
      if (!event.getAttendees().isEmpty()) {
        for (String attendee : requestAttendees) {
          if (event.getAttendees().contains(attendee)) {
            alreadyBookedTimes.add(event.getWhen());
            break;
          }
        }
      }
    }
    return alreadyBookedTimes;
  }

  // return the occupied time ranges including optional attendee schedules uses list of mandatory attendee time ranges to save time
  public ArrayList<TimeRange> getAlreadyBookedTimesWithOptional(Collection<Event> events, MeetingRequest request, ArrayList<TimeRange> withoutOptional) {
    Collection<String> optionalAttendees = request.getOptionalAttendees();   
    ArrayList<TimeRange> alreadyBookedTimesWithOptional = new ArrayList<TimeRange>();
    alreadyBookedTimesWithOptional.addAll(withoutOptional);
    if (!optionalAttendees.isEmpty()) {
      for (Event event : events) {
        for (String attendee : optionalAttendees) {
          if (event.getAttendees().contains(attendee)) {
            alreadyBookedTimesWithOptional.add(event.getWhen());
            break;
          } 
        }
      } 
    }
    return alreadyBookedTimesWithOptional; 
  }

  // return all the time ranges that are not occupied 
  public ArrayList<TimeRange> addAvailableRanges(MeetingRequest request, ArrayList<TimeRange> occupiedTimes) {
    long requestDuration = request.getDuration();
    ArrayList<TimeRange> availableRanges = new ArrayList<TimeRange>();
    int newStart;
    int newEnd;
    if (occupiedTimes.isEmpty()) {
      return occupiedTimes;
    }
    // if there is adequate time between the beginning of the day and the beginning of the first meeting add that time range to the output list   
    if (occupiedTimes.get(0).start() - TimeRange.START_OF_DAY >= requestDuration) {
      newStart = TimeRange.START_OF_DAY;
      newEnd = occupiedTimes.get(0).start();
      availableRanges.add(TimeRange.fromStartEnd(newStart, newEnd, false));    
    }
    // if there are adequate time between meetings add those time ranges to the output list
    for (int i = 0; i < occupiedTimes.size() - 1; i++) {
      if (occupiedTimes.get(i + 1).start() - occupiedTimes.get(i).end() >= requestDuration) {
        newStart = occupiedTimes.get(i).end();
        newEnd = occupiedTimes.get(i + 1).start();
        availableRanges.add(TimeRange.fromStartEnd(newStart, newEnd, false));
      }
    }
    // if there is adequate time between the end of the day and the end of the last meeting add that time range to the output list   
    if (TimeRange.END_OF_DAY - occupiedTimes.get(occupiedTimes.size() - 1).end() >= requestDuration) {
      newStart = occupiedTimes.get(occupiedTimes.size() - 1).end();
      newEnd = TimeRange.END_OF_DAY;
      availableRanges.add(TimeRange.fromStartEnd(newStart,newEnd , true));
    }
    return availableRanges;
  }
}
