public interface SmartClassroomDevice {
  // Fat interface (ISP violation)
}

interface PowerSwitchable extends SmartClassroomDevice {
  void powerOn();

  void powerOff();
}

interface BrightnessAdjustable extends SmartClassroomDevice {
  void setBrightness(int pct);
}

interface TemperatureAdjustable extends SmartClassroomDevice {
  void setTemperatureC(int c);
}

interface AttendanceCapable extends SmartClassroomDevice {
  int scanAttendance();
}

interface InputConnectable extends SmartClassroomDevice {
  void connectInput(String port);
}
