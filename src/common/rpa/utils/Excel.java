/**
 * 
 */
package common.rpa.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.rpa.exception.PruebaBotException;

/**
 * @author yeestevez
 *
 */
public class Excel {

	private static Logger logger = Logger.getLogger(Excel.class);

	/**
	 * Actualiza la celda indicada, sino existe la crea.
	 *
	 * @param excel
	 * @param hoja
	 * @param row
	 * @param col
	 * @param valor
	 * @return boolean
	 */
	public static boolean updateCell(String path, String hoja, int row, int col, Object valor) {
		try (FileInputStream inputFile = new FileInputStream(path);
				XSSFWorkbook workbook = new XSSFWorkbook(inputFile);) {
			XSSFSheet sheet = workbook.getSheet(hoja);
			Cell cell = null;
			cell = sheet.getRow(row).getCell(col);
			if (cell == null) {
				cell = sheet.getRow(row).createCell(col);
			}
			if (valor.getClass().getName().contains("Integer")) {
				cell.setCellValue((int) valor);
			} else if (valor.getClass().getName().contains("String")) {
				cell.setCellValue((String) valor);
			} else if (valor.getClass().getName().contains("Double")) {
				cell.setCellValue((Double) valor);
			} else if (valor.getClass().getName().contains("Date")) {
				CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
				style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
				cell.setCellStyle(style);
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue((Date) valor);
			} else {
				logger.error("Valor no admitido");
			}

			FileOutputStream outputFile = new FileOutputStream(path);
			workbook.write(outputFile);
			outputFile.close();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * Inserta una fila en la ultima posicion con los datos recibidos.
	 *
	 * @param path
	 * @param sheetName
	 * @param data
	 * @return boolean
	 */
	public static boolean inserRowData(String path, String sheetName, List<Object> data) {
		try (FileInputStream fis = new FileInputStream(path); XSSFWorkbook workbook = new XSSFWorkbook(fis);) {
			XSSFSheet sheet = workbook.getSheet(sheetName);
			int latest = sheet.getLastRowNum() + 1;
			insertRow(path, sheetName, latest);
			for (int i = 0; i < data.size(); i++) {
				updateCell(path, sheetName, latest, i, data.get(i));
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * Inserta una fila vacia en la posición indicada.
	 *
	 * @param path
	 * @param sheetName
	 * @param position
	 * @return boolean
	 */
	public static boolean insertRow(String path, String sheetName, int position) {
		try (FileInputStream fis = new FileInputStream(path); XSSFWorkbook workbook = new XSSFWorkbook(fis);) {
			XSSFSheet sheet = workbook.getSheet(sheetName);
			sheet.createRow(position);
			FileOutputStream outputFile = new FileOutputStream(path);
			workbook.write(outputFile);
			outputFile.close();

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		return true;
	}

	/**
	 * Obtiene todos los datos de la hoja especificada.
	 *
	 * @param path
	 * @param sheetName
	 * @return sheetData
	 * @throws HolkStroreEception
	 */
	public static List<ArrayList<String>> readAll(String path, String sheetName) throws PruebaBotException {

		List<ArrayList<String>> sheetData = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(path); XSSFWorkbook workbook = new XSSFWorkbook(fis);) {

			XSSFSheet sheet = workbook.getSheet(sheetName);
			int lastRow = sheet.getLastRowNum();
			for (int i = 0; i <= lastRow; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null) {
					row = sheet.createRow(i);
					XSSFCell newCell = row.getCell(0);
					if (newCell == null) {
						newCell = row.createCell(0);
					}

				}
				int lastCell = row.getLastCellNum();
				ArrayList<String> data = new ArrayList<>();
				for (int j = 0; j <= lastCell; j++) {
					String value = getCell(row.getCell(j), workbook);
					if (value != null) {
						data.add(value);
					} else {
						data.add("");
					}
				}
				if (!(data.isEmpty())) {
					sheetData.add(data);
				}
			}
		} catch (IOException e) {

			logger.error(e.getMessage(), e);

		}
		return sheetData;
	}

	/**
	 * Obtiene el valor de una celda.
	 *
	 * @param cell
	 * @return Object
	 */
	public static String getCell(XSSFCell cell, Workbook wb) {
		FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					return sdf.format(cell.getDateCellValue());
				} else {
					Locale.setDefault(Locale.US);
					DecimalFormat num = new DecimalFormat("####");
					return "" + num.format(cell.getNumericCellValue());
				}
			} else if (cell.getCellTypeEnum() == CellType.FORMULA) {

				switch (formulaEvaluator.evaluateInCell(cell).getCellTypeEnum()) {
				case NUMERIC:

					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						return sdf.format(cell.getDateCellValue());
					} else {
						Locale.setDefault(Locale.US);
						DecimalFormat num = new DecimalFormat("####");
						return "" + num.format(cell.getNumericCellValue());
					}
				case STRING:

					return cell.getStringCellValue();

				default:
					break;

				}
			} else if (cell.getCellTypeEnum() == CellType.STRING) {
				return cell.getStringCellValue();
			}
		}

		return null;
	}
}
