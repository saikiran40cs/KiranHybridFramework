package testCases;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import excelExportAndFileIO.KiranExcelFileReader;
import operation.ReadObject;
import operation.UIOperation;

/**
 * @author saikiran40cs
 * THIS IS THE EXAMPLE OF KEYWORD WITH DATA DRIVEN TEST CASE
 *
 */
public class TC001_BorlandHybridTest {

	DesiredCapabilities ChromeCapabilities = DesiredCapabilities.chrome();
	WebDriver webdriver;

	@BeforeTest
	public void browserSetup() {
		ChromeOptions chromeOptions = new ChromeOptions();
		
		ChromeCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		ChromeCapabilities.setCapability("network.proxy.type", ProxyType.AUTODETECT.ordinal());
		// Set ACCEPT_SSL_CERTS variable to true
		ChromeCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		ChromeCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true); 
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_settings.popups", 0);
		prefs.put("download.extensions_to_open", "pdf");
		prefs.put("download.prompt_for_download", "true");
		chromeOptions.setExperimentalOption("prefs", prefs);
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator+"chromedriver_2.30.exe");
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		
	}

	/**
	 * Read Row by row from Excel and execute the test step
	 * @param testcaseName
	 * @param keyword
	 * @param objectName
	 * @param objectType
	 * @param value
	 * @throws Exception
	 */
	@Test(dataProvider = "hybridData")
	public void testLogin(String testcaseName, String keyword, String objectName, String objectType, String value)
			throws Exception {
		if (testcaseName != null && testcaseName.length() != 0) {
			webdriver = new ChromeDriver(ChromeCapabilities);
			webdriver.manage().window().maximize();
		}
		ReadObject object = new ReadObject();
		Properties allObjects = object.getObjectRepository();
		UIOperation operation = new UIOperation(webdriver);
		// Call perform function to perform operation on UI
		operation.perform(allObjects, keyword, objectName, objectType, value);

	}

	@DataProvider(name = "hybridData")
	public Object[][] getDataFromDataprovider() throws IOException {
		Object[][] object = null;
		KiranExcelFileReader file = new KiranExcelFileReader();

		// Read keyword sheet
		Sheet KiranSheet = file.readExcel(System.getProperty("user.dir") + File.separator, "TestCase.xlsx","Hybrid_Key_Framework");
		// Find number of rows in excel file
		int rowCount = KiranSheet.getLastRowNum() - KiranSheet.getFirstRowNum();
		object = new Object[rowCount][5];
		for (int i = 0; i < rowCount; i++) {
			// Loop over all the rows
			Row row = KiranSheet.getRow(i + 1);
			// Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				// Print excel data in console
				object[i][j] = row.getCell(j).toString();
			}
		}
		return object;
	}
	
	@AfterTest
	public void closeSession(){
		webdriver.close();
	}

}
