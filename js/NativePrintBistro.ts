import type { TurboModule } from 'react-native/Libraries/TurboModule/RCTExport';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  isAppInstalled(): Promise<boolean>;
  startAppByPackageName(packageName: string): Promise<boolean>;
  getPrinters(): Promise<string>;
  print(printerCode: string, jsonInput: string): Promise<string>;
}

export default TurboModuleRegistry.get<Spec>(
  'RTNPrintBistro'
) as Spec | {
  isAppInstalled(): Promise<boolean>;
  startAppByPackageName(packageName: string): Promise<boolean>;
  getPrinters(): Promise<string>;
  print(printerCode: string, jsonInput: string): Promise<string>;
}
