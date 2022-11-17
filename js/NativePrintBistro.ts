import type { TurboModule } from 'react-native/Libraries/TurboModule/RCTExport';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  startKioskMode(): void;
  stopKioskMode(): void;
  closeApp(): void;
  checkPermissions(): void;
}

export default TurboModuleRegistry.get<Spec>(
  'RTNPrintBistro'
) as Spec | {
  startKioskMode: () => {},
  stopKioskMode: () => {},
  closeApp: () => {},
  checkPermissions: () => {},
}