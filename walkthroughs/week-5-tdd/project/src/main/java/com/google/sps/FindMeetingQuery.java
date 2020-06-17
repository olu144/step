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
    long duration = request.getDuration();
    Collection<String> attendees = request.getAttendees();
    ArrayList<TimeRange> validTimes = new ArrayList<TimeRange>();
    ArrayList<TimeRange> output = new ArrayList<TimeRange>();  
    ArrayList<TimeRange> finalOutput = new ArrayList<TimeRange>();  
    int newStart;
    int newEnd;

    if (attendees.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    if (duration >= TimeRange.WHOLE_DAY.duration()) {
      return Arrays.asList();  
    }

    for (Event e: events) {
      if (!e.getAttendees().isEmpty()) {
        validTimes.add(e.getWhen());
        }
    }  

    if (validTimes.size() > 1){
      for (int i = 1; i < validTimes.size(); i++){
        if (validTimes.get(i-1).overlaps(validTimes.get(i))){
          newStart = Math.min(validTimes.get(i-1).start(), validTimes.get(i).start());
          newEnd = Math.max(validTimes.get(i).end(), validTimes.get(i-1).end());
          output.add(TimeRange.fromStartEnd(newStart, newEnd, false));
        } else {
          output.add(validTimes.get(i-1));
          output.add(validTimes.get(i));
        }
      }
    } else {
      output = validTimes;
    }

    if(output.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);    
    }

    if(output.get(0).start() - TimeRange.START_OF_DAY >= duration) {
      newStart = TimeRange.START_OF_DAY;
      newEnd = output.get(0).start();
      finalOutput.add(TimeRange.fromStartEnd(newStart, newEnd, false));    
    }

    for (int i = 0; i < output.size() - 1; i++) {
      if(output.get(i+1).start() - output.get(i).end() >= duration){
          newStart = output.get(i).end();
          newEnd = output.get(i+1).start();
          finalOutput.add(TimeRange.fromStartEnd(newStart, newEnd, false));
      }
    }

    if(TimeRange.END_OF_DAY - output.get(output.size() - 1).end() >= duration) {
        newStart = output.get(output.size()-1).end();
        newEnd = TimeRange.END_OF_DAY;
      finalOutput.add(TimeRange.fromStartEnd(newStart,newEnd , true));
    }

    return finalOutput;

  }
}