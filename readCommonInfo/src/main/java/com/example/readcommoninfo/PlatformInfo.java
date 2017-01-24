package com.example.readcommoninfo;

import java.io.Serializable;
import java.util.Date;

public class PlatformInfo implements Serializable {
    private Integer id;

    private String eId;

    private String custKey;

    private String imei;

    private String wmac;

    private String aid;

    private String simImsi;

    private String simSerial;

    private String simNumber;

    private String simOperator;

    private String simOperatorName;

    private String simCountryIso;

    private Integer simState;

    private String telCellinfo;

    private String telNeighboringCellinfo;

    private String telAllCellinfo;

    private Integer telPhoneType;

    private Integer telPhoneCount;

    private String telOperatorName;

    private String telOperator;

    private Byte telIsRoaming;

    private String telCountryIso;

    private Integer telNetworkType;

    private Byte telHasIcccard;

    private String telLine1Number;

    private String telVmAlphatag;

    private String telVmNumber;

    private Integer telVoiceNetworkType;

    private String telMmsUa;

    private String telMmsUaProfurl;

    private Byte telHasCarrierPri;

    private String telDeviceId;

    private String telDeviceVersion;

    private Long mem;

    private Long rom;

    private Long memAvailsize;

    private Integer romBlocksize;

    private Integer romTotalblocks;

    private Integer romAvailblocks;

    private Integer sdBlocksize;

    private Integer sdTotalblocks;

    private Integer sdAvailblocks;

    private Short screenWidth;

    private Short screenHeight;

    private String resolution;

    private String bid;

    private String bdisplay;

    private String bproduct;

    private String bdevice;

    private String bboard;

    private String bmanufacture;

    private String bbrand;

    private String bmodel;

    private String bbootloader;

    private String bhardware;

    private String bserial;

    private String bvInc;

    private String bvRel;

    private String bvSdkInt;

    private String bvCode;

    private String btype;

    private String btags;

    private String bfingerprint;

    private String btime;

    private String buser;

    private String bhost;

    private String bradio;

    private String channel;

    private String essid;

    private String bssid;

    private String ipv4;

    private String ipv6;

    private String ua;

    private String network;

    private String usbSerial;

    private String usbManufacture;

    private String usbProduct;

    private String usbIdProduct;

    private String usbIdVendor;

    private String cameraInfo;

    private String torchInfo;

    private String cpuInfo;

    private String gpuInfo;

    private String memInfo;

    private String lcdInfo;

    private String tpInfo;

    private String gsensorInfo;

    private String msensorInfo;

    private String dsensorInfo;

    private String gyroInfo;

    private String lightInfo;

    private String proximityInfo;

    private Integer gpuFilesize;

    private String gpuFilepath;

    private Integer camFilesize;

    private String camFilepath;

    private Integer appFilesize;

    private String appFilepath;

    private Integer treeFilesize;

    private String treeFilepath;

    private Integer procFilesize;

    private String procFilepath;

    private Integer propFilesize;

    private String propFilepath;

    private String versionName;

    private Integer versionCode;

    private String publicIp;

    private String publicIpInfo;

    private Integer accessCount;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId == null ? null : eId.trim();
    }

    public String getCustKey() {
        return custKey;
    }

    public void setCustKey(String custKey) {
        this.custKey = custKey == null ? null : custKey.trim();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getWmac() {
        return wmac;
    }

    public void setWmac(String wmac) {
        this.wmac = wmac == null ? null : wmac.trim();
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid == null ? null : aid.trim();
    }

    public String getSimImsi() {
        return simImsi;
    }

    public void setSimImsi(String simImsi) {
        this.simImsi = simImsi == null ? null : simImsi.trim();
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial == null ? null : simSerial.trim();
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber == null ? null : simNumber.trim();
    }

    public String getSimOperator() {
        return simOperator;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator == null ? null : simOperator.trim();
    }

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName == null ? null : simOperatorName.trim();
    }

    public String getSimCountryIso() {
        return simCountryIso;
    }

    public void setSimCountryIso(String simCountryIso) {
        this.simCountryIso = simCountryIso == null ? null : simCountryIso.trim();
    }

    public Integer getSimState() {
        return simState;
    }

    public void setSimState(Integer simState) {
        this.simState = simState;
    }

    public String getTelCellinfo() {
        return telCellinfo;
    }

    public void setTelCellinfo(String telCellinfo) {
        this.telCellinfo = telCellinfo == null ? null : telCellinfo.trim();
    }

    public String getTelNeighboringCellinfo() {
        return telNeighboringCellinfo;
    }

    public void setTelNeighboringCellinfo(String telNeighboringCellinfo) {
        this.telNeighboringCellinfo = telNeighboringCellinfo == null ? null : telNeighboringCellinfo.trim();
    }

    public String getTelAllCellinfo() {
        return telAllCellinfo;
    }

    public void setTelAllCellinfo(String telAllCellinfo) {
        this.telAllCellinfo = telAllCellinfo == null ? null : telAllCellinfo.trim();
    }

    public Integer getTelPhoneType() {
        return telPhoneType;
    }

    public void setTelPhoneType(Integer telPhoneType) {
        this.telPhoneType = telPhoneType;
    }

    public Integer getTelPhoneCount() {
        return telPhoneCount;
    }

    public void setTelPhoneCount(Integer telPhoneCount) {
        this.telPhoneCount = telPhoneCount;
    }

    public String getTelOperatorName() {
        return telOperatorName;
    }

    public void setTelOperatorName(String telOperatorName) {
        this.telOperatorName = telOperatorName == null ? null : telOperatorName.trim();
    }

    public String getTelOperator() {
        return telOperator;
    }

    public void setTelOperator(String telOperator) {
        this.telOperator = telOperator == null ? null : telOperator.trim();
    }

    public Byte getTelIsRoaming() {
        return telIsRoaming;
    }

    public void setTelIsRoaming(Byte telIsRoaming) {
        this.telIsRoaming = telIsRoaming;
    }

    public String getTelCountryIso() {
        return telCountryIso;
    }

    public void setTelCountryIso(String telCountryIso) {
        this.telCountryIso = telCountryIso == null ? null : telCountryIso.trim();
    }

    public Integer getTelNetworkType() {
        return telNetworkType;
    }

    public void setTelNetworkType(Integer telNetworkType) {
        this.telNetworkType = telNetworkType;
    }

    public Byte getTelHasIcccard() {
        return telHasIcccard;
    }

    public void setTelHasIcccard(Byte telHasIcccard) {
        this.telHasIcccard = telHasIcccard;
    }

    public String getTelLine1Number() {
        return telLine1Number;
    }

    public void setTelLine1Number(String telLine1Number) {
        this.telLine1Number = telLine1Number == null ? null : telLine1Number.trim();
    }

    public String getTelVmAlphatag() {
        return telVmAlphatag;
    }

    public void setTelVmAlphatag(String telVmAlphatag) {
        this.telVmAlphatag = telVmAlphatag == null ? null : telVmAlphatag.trim();
    }

    public String getTelVmNumber() {
        return telVmNumber;
    }

    public void setTelVmNumber(String telVmNumber) {
        this.telVmNumber = telVmNumber == null ? null : telVmNumber.trim();
    }

    public Integer getTelVoiceNetworkType() {
        return telVoiceNetworkType;
    }

    public void setTelVoiceNetworkType(Integer telVoiceNetworkType) {
        this.telVoiceNetworkType = telVoiceNetworkType;
    }

    public String getTelMmsUa() {
        return telMmsUa;
    }

    public void setTelMmsUa(String telMmsUa) {
        this.telMmsUa = telMmsUa == null ? null : telMmsUa.trim();
    }

    public String getTelMmsUaProfurl() {
        return telMmsUaProfurl;
    }

    public void setTelMmsUaProfurl(String telMmsUaProfurl) {
        this.telMmsUaProfurl = telMmsUaProfurl == null ? null : telMmsUaProfurl.trim();
    }

    public Byte getTelHasCarrierPri() {
        return telHasCarrierPri;
    }

    public void setTelHasCarrierPri(Byte telHasCarrierPri) {
        this.telHasCarrierPri = telHasCarrierPri;
    }

    public String getTelDeviceId() {
        return telDeviceId;
    }

    public void setTelDeviceId(String telDeviceId) {
        this.telDeviceId = telDeviceId == null ? null : telDeviceId.trim();
    }

    public String getTelDeviceVersion() {
        return telDeviceVersion;
    }

    public void setTelDeviceVersion(String telDeviceVersion) {
        this.telDeviceVersion = telDeviceVersion == null ? null : telDeviceVersion.trim();
    }

    public Long getMem() {
        return mem;
    }

    public void setMem(Long mem) {
        this.mem = mem;
    }

    public Long getRom() {
        return rom;
    }

    public void setRom(Long rom) {
        this.rom = rom;
    }

    public Long getMemAvailsize() {
        return memAvailsize;
    }

    public void setMemAvailsize(Long memAvailsize) {
        this.memAvailsize = memAvailsize;
    }

    public Integer getRomBlocksize() {
        return romBlocksize;
    }

    public void setRomBlocksize(Integer romBlocksize) {
        this.romBlocksize = romBlocksize;
    }

    public Integer getRomTotalblocks() {
        return romTotalblocks;
    }

    public void setRomTotalblocks(Integer romTotalblocks) {
        this.romTotalblocks = romTotalblocks;
    }

    public Integer getRomAvailblocks() {
        return romAvailblocks;
    }

    public void setRomAvailblocks(Integer romAvailblocks) {
        this.romAvailblocks = romAvailblocks;
    }

    public Integer getSdBlocksize() {
        return sdBlocksize;
    }

    public void setSdBlocksize(Integer sdBlocksize) {
        this.sdBlocksize = sdBlocksize;
    }

    public Integer getSdTotalblocks() {
        return sdTotalblocks;
    }

    public void setSdTotalblocks(Integer sdTotalblocks) {
        this.sdTotalblocks = sdTotalblocks;
    }

    public Integer getSdAvailblocks() {
        return sdAvailblocks;
    }

    public void setSdAvailblocks(Integer sdAvailblocks) {
        this.sdAvailblocks = sdAvailblocks;
    }

    public Short getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Short screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Short getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Short screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution == null ? null : resolution.trim();
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid == null ? null : bid.trim();
    }

    public String getBdisplay() {
        return bdisplay;
    }

    public void setBdisplay(String bdisplay) {
        this.bdisplay = bdisplay == null ? null : bdisplay.trim();
    }

    public String getBproduct() {
        return bproduct;
    }

    public void setBproduct(String bproduct) {
        this.bproduct = bproduct == null ? null : bproduct.trim();
    }

    public String getBdevice() {
        return bdevice;
    }

    public void setBdevice(String bdevice) {
        this.bdevice = bdevice == null ? null : bdevice.trim();
    }

    public String getBboard() {
        return bboard;
    }

    public void setBboard(String bboard) {
        this.bboard = bboard == null ? null : bboard.trim();
    }

    public String getBmanufacture() {
        return bmanufacture;
    }

    public void setBmanufacture(String bmanufacture) {
        this.bmanufacture = bmanufacture == null ? null : bmanufacture.trim();
    }

    public String getBbrand() {
        return bbrand;
    }

    public void setBbrand(String bbrand) {
        this.bbrand = bbrand == null ? null : bbrand.trim();
    }

    public String getBmodel() {
        return bmodel;
    }

    public void setBmodel(String bmodel) {
        this.bmodel = bmodel == null ? null : bmodel.trim();
    }

    public String getBbootloader() {
        return bbootloader;
    }

    public void setBbootloader(String bbootloader) {
        this.bbootloader = bbootloader == null ? null : bbootloader.trim();
    }

    public String getBhardware() {
        return bhardware;
    }

    public void setBhardware(String bhardware) {
        this.bhardware = bhardware == null ? null : bhardware.trim();
    }

    public String getBserial() {
        return bserial;
    }

    public void setBserial(String bserial) {
        this.bserial = bserial == null ? null : bserial.trim();
    }

    public String getBvInc() {
        return bvInc;
    }

    public void setBvInc(String bvInc) {
        this.bvInc = bvInc == null ? null : bvInc.trim();
    }

    public String getBvRel() {
        return bvRel;
    }

    public void setBvRel(String bvRel) {
        this.bvRel = bvRel == null ? null : bvRel.trim();
    }

    public String getBvSdkInt() {
        return bvSdkInt;
    }

    public void setBvSdkInt(String bvSdkInt) {
        this.bvSdkInt = bvSdkInt == null ? null : bvSdkInt.trim();
    }

    public String getBvCode() {
        return bvCode;
    }

    public void setBvCode(String bvCode) {
        this.bvCode = bvCode == null ? null : bvCode.trim();
    }

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype == null ? null : btype.trim();
    }

    public String getBtags() {
        return btags;
    }

    public void setBtags(String btags) {
        this.btags = btags == null ? null : btags.trim();
    }

    public String getBfingerprint() {
        return bfingerprint;
    }

    public void setBfingerprint(String bfingerprint) {
        this.bfingerprint = bfingerprint == null ? null : bfingerprint.trim();
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime == null ? null : btime.trim();
    }

    public String getBuser() {
        return buser;
    }

    public void setBuser(String buser) {
        this.buser = buser == null ? null : buser.trim();
    }

    public String getBhost() {
        return bhost;
    }

    public void setBhost(String bhost) {
        this.bhost = bhost == null ? null : bhost.trim();
    }

    public String getBradio() {
        return bradio;
    }

    public void setBradio(String bradio) {
        this.bradio = bradio == null ? null : bradio.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public String getEssid() {
        return essid;
    }

    public void setEssid(String essid) {
        this.essid = essid == null ? null : essid.trim();
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid == null ? null : bssid.trim();
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4 == null ? null : ipv4.trim();
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6 == null ? null : ipv6.trim();
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua == null ? null : ua.trim();
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network == null ? null : network.trim();
    }

    public String getUsbSerial() {
        return usbSerial;
    }

    public void setUsbSerial(String usbSerial) {
        this.usbSerial = usbSerial == null ? null : usbSerial.trim();
    }

    public String getUsbManufacture() {
        return usbManufacture;
    }

    public void setUsbManufacture(String usbManufacture) {
        this.usbManufacture = usbManufacture == null ? null : usbManufacture.trim();
    }

    public String getUsbProduct() {
        return usbProduct;
    }

    public void setUsbProduct(String usbProduct) {
        this.usbProduct = usbProduct == null ? null : usbProduct.trim();
    }

    public String getUsbIdProduct() {
        return usbIdProduct;
    }

    public void setUsbIdProduct(String usbIdProduct) {
        this.usbIdProduct = usbIdProduct == null ? null : usbIdProduct.trim();
    }

    public String getUsbIdVendor() {
        return usbIdVendor;
    }

    public void setUsbIdVendor(String usbIdVendor) {
        this.usbIdVendor = usbIdVendor == null ? null : usbIdVendor.trim();
    }

    public String getCameraInfo() {
        return cameraInfo;
    }

    public void setCameraInfo(String cameraInfo) {
        this.cameraInfo = cameraInfo == null ? null : cameraInfo.trim();
    }

    public String getTorchInfo() {
        return torchInfo;
    }

    public void setTorchInfo(String torchInfo) {
        this.torchInfo = torchInfo == null ? null : torchInfo.trim();
    }

    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo == null ? null : cpuInfo.trim();
    }

    public String getGpuInfo() {
        return gpuInfo;
    }

    public void setGpuInfo(String gpuInfo) {
        this.gpuInfo = gpuInfo == null ? null : gpuInfo.trim();
    }

    public String getMemInfo() {
        return memInfo;
    }

    public void setMemInfo(String memInfo) {
        this.memInfo = memInfo == null ? null : memInfo.trim();
    }

    public String getLcdInfo() {
        return lcdInfo;
    }

    public void setLcdInfo(String lcdInfo) {
        this.lcdInfo = lcdInfo == null ? null : lcdInfo.trim();
    }

    public String getTpInfo() {
        return tpInfo;
    }

    public void setTpInfo(String tpInfo) {
        this.tpInfo = tpInfo == null ? null : tpInfo.trim();
    }

    public String getGsensorInfo() {
        return gsensorInfo;
    }

    public void setGsensorInfo(String gsensorInfo) {
        this.gsensorInfo = gsensorInfo == null ? null : gsensorInfo.trim();
    }

    public String getMsensorInfo() {
        return msensorInfo;
    }

    public void setMsensorInfo(String msensorInfo) {
        this.msensorInfo = msensorInfo == null ? null : msensorInfo.trim();
    }

    public String getDsensorInfo() {
        return dsensorInfo;
    }

    public void setDsensorInfo(String dsensorInfo) {
        this.dsensorInfo = dsensorInfo == null ? null : dsensorInfo.trim();
    }

    public String getGyroInfo() {
        return gyroInfo;
    }

    public void setGyroInfo(String gyroInfo) {
        this.gyroInfo = gyroInfo == null ? null : gyroInfo.trim();
    }

    public String getLightInfo() {
        return lightInfo;
    }

    public void setLightInfo(String lightInfo) {
        this.lightInfo = lightInfo == null ? null : lightInfo.trim();
    }

    public String getProximityInfo() {
        return proximityInfo;
    }

    public void setProximityInfo(String proximityInfo) {
        this.proximityInfo = proximityInfo == null ? null : proximityInfo.trim();
    }

    public Integer getGpuFilesize() {
        return gpuFilesize;
    }

    public void setGpuFilesize(Integer gpuFilesize) {
        this.gpuFilesize = gpuFilesize;
    }

    public String getGpuFilepath() {
        return gpuFilepath;
    }

    public void setGpuFilepath(String gpuFilepath) {
        this.gpuFilepath = gpuFilepath == null ? null : gpuFilepath.trim();
    }

    public Integer getCamFilesize() {
        return camFilesize;
    }

    public void setCamFilesize(Integer camFilesize) {
        this.camFilesize = camFilesize;
    }

    public String getCamFilepath() {
        return camFilepath;
    }

    public void setCamFilepath(String camFilepath) {
        this.camFilepath = camFilepath == null ? null : camFilepath.trim();
    }

    public Integer getAppFilesize() {
        return appFilesize;
    }

    public void setAppFilesize(Integer appFilesize) {
        this.appFilesize = appFilesize;
    }

    public String getAppFilepath() {
        return appFilepath;
    }

    public void setAppFilepath(String appFilepath) {
        this.appFilepath = appFilepath == null ? null : appFilepath.trim();
    }

    public Integer getTreeFilesize() {
        return treeFilesize;
    }

    public void setTreeFilesize(Integer treeFilesize) {
        this.treeFilesize = treeFilesize;
    }

    public String getTreeFilepath() {
        return treeFilepath;
    }

    public void setTreeFilepath(String treeFilepath) {
        this.treeFilepath = treeFilepath == null ? null : treeFilepath.trim();
    }

    public Integer getProcFilesize() {
        return procFilesize;
    }

    public void setProcFilesize(Integer procFilesize) {
        this.procFilesize = procFilesize;
    }

    public String getProcFilepath() {
        return procFilepath;
    }

    public void setProcFilepath(String procFilepath) {
        this.procFilepath = procFilepath == null ? null : procFilepath.trim();
    }

    public Integer getPropFilesize() {
        return propFilesize;
    }

    public void setPropFilesize(Integer propFilesize) {
        this.propFilesize = propFilesize;
    }

    public String getPropFilepath() {
        return propFilepath;
    }

    public void setPropFilepath(String propFilepath) {
        this.propFilepath = propFilepath == null ? null : propFilepath.trim();
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? null : versionName.trim();
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp == null ? null : publicIp.trim();
    }

    public String getPublicIpInfo() {
        return publicIpInfo;
    }

    public void setPublicIpInfo(String publicIpInfo) {
        this.publicIpInfo = publicIpInfo == null ? null : publicIpInfo.trim();
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}