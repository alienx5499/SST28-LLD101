import java.util.*;

public class DeviceRegistry {
  private final java.util.List<SmartClassroomDevice> devices = new ArrayList<>();

  public void add(SmartClassroomDevice d) {
    devices.add(d);
  }

  public <T> T getSingleByCapability(Class<T> capabilityType) {
    T found = null;
    for (SmartClassroomDevice d : devices) {
      if (capabilityType.isInstance(d)) {
        if (found != null) {
          throw new IllegalStateException(
              "Multiple devices implement " + capabilityType.getSimpleName());
        }
        found = capabilityType.cast(d);
      }
    }
    if (found == null) {
      throw new IllegalStateException("Missing device for " + capabilityType.getSimpleName());
    }
    return found;
  }

  public <T> List<T> getAllByCapability(Class<T> capabilityType) {
    List<T> result = new ArrayList<>();
    for (SmartClassroomDevice d : devices) {
      if (capabilityType.isInstance(d)) {
        result.add(capabilityType.cast(d));
      }
    }
    return result;
  }
}
