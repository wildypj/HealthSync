package com.wildy.doctor.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/{doctorId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // get method for doctor by ID and all schedule
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules(@PathVariable("doctorId") Long doctorId) {
        List<Schedule> doctorSchedule = scheduleService.getAllDoctorSchedules(doctorId);
        return ResponseEntity.ok(doctorSchedule);
    }

    // get method for doctor by ID and schedules by specific date
    @GetMapping("/date")
    public ResponseEntity<List<Schedule>> getSchedulesByDate(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam String date){
       try{
           LocalDate parsedDate = LocalDate.parse(date);
           List<Schedule> doctorScheduleByDate = scheduleService.getDoctorScheduleByDate(doctorId, parsedDate);
           if(doctorScheduleByDate.isEmpty()) {
               return ResponseEntity.noContent().build();
           }
           return new ResponseEntity<>(doctorScheduleByDate, HttpStatus.OK);
       }catch (DateTimeParseException e){
//           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build("Invalid date format. Please use yyyy-MM-dd.");
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }


    // create modify schedules
    @PostMapping
    public ResponseEntity<String> createSchedule(
            @PathVariable Long doctorId,
            @RequestBody Schedule newSchedule) {
        try {
            scheduleService.createSchedule(doctorId, newSchedule);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Schedule created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Update schedules
    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> updateSchedule(
            @PathVariable Long doctorId,
            @PathVariable Long scheduleId,
            @RequestBody Schedule updatedSchedule
    ){
        try {
            scheduleService.updateSchedule(doctorId, scheduleId, updatedSchedule);
            return ResponseEntity.ok("Schedule updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // delete schedules
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(
            @PathVariable Long doctorId,
            @PathVariable Long scheduleId) {

        try {
            scheduleService.deleteSchedule(doctorId, scheduleId);
            return ResponseEntity.ok("Schedule deleted successfully");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

// Notify other services about schedule changes.
