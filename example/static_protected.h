##include "platform.h"

namespace v8 {
namespace base {

class PosixTimezoneCache : public TimezoneCache {
 public:
  double DaylightSavingsOffset(double time_ms) override;
  void Clear(TimeZoneDetection) override {}
  ~PosixTimezoneCache() override = default;

 protected:
  static const int msPerSecond = 1000;
};

}  // namespace base
}  // namespace v8
