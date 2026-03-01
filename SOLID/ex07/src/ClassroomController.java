public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        InputConnectable pj = reg.getSingleByCapability(InputConnectable.class);
        if (pj instanceof PowerSwitchable) {
            ((PowerSwitchable) pj).powerOn();
        }
        pj.connectInput("HDMI-1");

        BrightnessAdjustable lights = reg.getSingleByCapability(BrightnessAdjustable.class);
        lights.setBrightness(60);

        TemperatureAdjustable ac = reg.getSingleByCapability(TemperatureAdjustable.class);
        ac.setTemperatureC(24);

        AttendanceCapable scan = reg.getSingleByCapability(AttendanceCapable.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        for (PowerSwitchable d : reg.getAllByCapability(PowerSwitchable.class)) {
            d.powerOff();
        }
    }
}
