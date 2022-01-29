package com.panpawelw.weightliftinglog.serviceIT;


import com.panpawelw.weightliftinglog.services.DBFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBFileServiceIT {

  @Autowired
  private DBFileService service;

  @Test
  public void storeAllFilesByWorkoutSucceeds() {

  }

  @Test
  public void getFileByWorkoutAndFilenameSucceeds() {

  }

  @Test
  public void deleteFileByWorkoutAndFilenameSucceeds() {

  }

  @Test
  public void deleteAllFilesByWorkoutIdSucceeds() {

  }
}
