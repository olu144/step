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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long duration = request.getDuration();
    Collection<String> attendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    ArrayList<Event> validEvents = new ArrayList<Event>();
    ArrayList<TimeRange> validTimes = new ArrayList<TimeRange>();
    ArrayList<TimeRange> output = new ArrayList<TimeRange>();
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
    Collections.sort(validTimes, TimeRange.ORDER_BY_START);
    for (int i = 1; i < validTimes.size(); i++){
        if(validTimes.get(i-1).overlaps(validTimes.get(i))){
            //output.add(new TimeRange(validTimes.get(i-1).start(), validTimes.get(1).end() - validTimes.get(i-1).start()));
        }
    } 
      return validTimes;
  }
}