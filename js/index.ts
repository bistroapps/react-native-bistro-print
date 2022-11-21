import printBistro from './NativePrintBistro';
import { pad, padEnd, padStart, truncate } from 'lodash';

export type PrinterType = {
  ready: boolean;
  id: string;
  name: string;
  manufacturer: string;
  type: 'inner' | 'external';
  columns: number;
};

type PrinterFromService = {
  codigo: string;
  fabricante: string;
  ip: string;
  nome: string;
  pid: string;
  tcpip: boolean;
  usb: boolean;
  vid: string;
  colunas: number;
};

export async function getPrint() {
  const isInstall = await printBistro.isAppInstalled(
    'br.com.bistroapps.print_service_app',
  );

  if (!isInstall) throw 'app not installed';

  const printers = await printBistro.getPrinters();

  const items: PrinterType[] = [];
  if (printers) {
    JSON.parse(printers).forEach((printer: PrinterFromService) => {
      items.push({
        ready: true,
        id: printer.codigo,
        manufacturer: printer.fabricante,
        name: printer.nome,
        type: 'external',
        columns: printer.colunas || 48,
      });
    });
  }

  return items;
}


export const createColumnsText = (
  columns: {
    text: string;
    align: 'left' | 'center' | 'right';
    flex: number;
  }[],
  columnsSize: number,
): string => {
  const flexSum = columns.reduce((sum, current) => sum + current.flex, 0);
  const marginLeftString = '';
  const marginRightString = '';
  const columnsSizeMinusMargin =
    columnsSize - marginLeftString.length - marginRightString.length;
  let columnUnit = Math.trunc(columnsSizeMinusMargin / flexSum);

  if (columnUnit < 1) columnUnit = 1;

  const text =
    columns.reduce((text, column) => {
      const columnText = truncate(column.text, {
        length: columnUnit * column.flex,
        omission: '',
      });
      return {
        left: `${text}${padEnd(columnText, columnUnit * column.flex)}`,
        center: `${text}${pad(columnText, columnUnit * column.flex)}`,
        right: `${text}${padStart(columnText, columnUnit * column.flex)}`,
      }[column.align];
    }, marginLeftString) + marginRightString;

  if (text.length < columnsSize) {
    return pad(text, columnsSize);
  }
  return text;
};

type Align = 'Center' | 'Left' | 'Right';

export type PrintProps = {
  header: string;
  qrcode?: { position: 'top' | 'bottom'; text: string; lines?: number };
  footer: { text: string; bold?: boolean; align?: Align };
  order: { text: string; bold?: boolean; align?: Align };
  hideTitles?: boolean;
  items?: { name: string; quantity: number; value: string }[];
  lastItem?: string;
  customer?: string;
  date?: string;
};

export type PrintConf = {
  columns: number, id: string
}

export async function onPrint(
  { columns, id }: PrintConf,
  {
    header,
    qrcode,
    footer,
    order,
    hideTitles,
    items,
    lastItem,
    customer,
    date,
  }: PrintProps) {
  const print: object[] = [];

  if (!columns || !id) {
    return 'Selecione uma impressora';
  }

  // header
  print.push({
    type: 'text',
    text: header,
    bold: true,
    align: 'Center',
    underscore: false,
    doubleWidth: false,
    doubleHeight: false,
    reverse: false,
  });
  print.push({ type: 'lines', lines: 2 });

  // order
  print.push({
    type: 'text',
    text: order.text,
    bold: order.bold || true,
    align: order.align || 'Center',
    underscore: false,
    doubleWidth: false,
    doubleHeight: false,
    reverse: false,
  });
  print.push({ type: 'lines', lines: 1 });

  // qr code top
  if (qrcode?.position === 'top') {
    print.push({
      type: 'qrcode',
      text: qrcode.text,
      lines: qrcode.lines || 20,
      align: 'Center',
    });
    print.push({ type: 'lines', lines: 1 });
  }

  if (!hideTitles) {
    const itemsTableHeadText = createColumnsText(
      [
        { align: 'left', flex: 0.5, text: 'Qtd' },
        { align: 'left', flex: 2.5, text: 'Item' },
        { align: 'center', flex: 1, text: 'Valor' },
      ],
      Number(columns),
    );

    print.push({
      type: 'text',
      text: itemsTableHeadText,
      bold: false,
      align: 'Center',
      underscore: true,
      doubleWidth: false,
      doubleHeight: false,
      reverse: false,
    });
  }

  if (items) {
    items.forEach(item => {
      const itemTableRowText = createColumnsText(
        [
          { align: 'left', flex: 0.5, text: `${item.quantity}` },
          { align: 'left', flex: 2.5, text: item.name },
          { align: 'right', flex: 1, text: item.value },
        ],
        Number(columns),
      );

      print.push({
        type: 'text',
        text: itemTableRowText,
        bold: true,
        align: 'Left',
        underscore: false,
        doubleWidth: false,
        doubleHeight: false,
        reverse: false,
      });
    });
  }

  if (lastItem) {
    print.push({
      type: 'text',
      text: lastItem,
      bold: true,
      align: 'Right',
      underscore: false,
      doubleWidth: false,
      doubleHeight: false,
      reverse: false,
    });
  }

  if (customer) {
    print.push({
      type: 'text',
      text: customer,
      bold: false,
      align: 'Center',
      underscore: false,
      doubleWidth: false,
      doubleHeight: false,
      reverse: false,
    });
  }

  if (date) {
    print.push({
      type: 'text',
      text: date,
      bold: false,
      align: 'Center',
      underscore: false,
      doubleWidth: false,
      doubleHeight: false,
      reverse: false,
    });
  }

  // qr code bottom
  if (qrcode?.position === 'bottom') {
    print.push({ type: 'lines', lines: 2 });
    print.push({
      type: 'qrcode',
      text: qrcode.text,
      lines: qrcode.lines || 20,
      align: 'Center',
    });
  }

  // footer
  print.push({ type: 'lines', lines: 2 });
  print.push({
    type: 'text',
    text: footer.text,
    bold: footer.bold || false,
    align: footer.align || 'Center',
    underscore: false,
    doubleWidth: false,
    doubleHeight: false,
    reverse: false,
  });
  print.push({ type: 'lines', lines: 2 });
  print.push({ type: 'cutter_part' });
  print.push({ type: 'beep' });

  // console.log(print);

  const result = await printBistro.print(String(id), JSON.stringify({ print }));

  return result;
}

