DESCRIPTION = "Webapp that can be accessed by connecting to the Ultra96 board using AP address and boards MAC address"
SUMMARY = "Webapp that provides support for Ultra96 devices"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=7b4e2c1c8ac64078f34f4eef8d3b4f46"

SRC_URI = "git://github.com/Xilinx/ultra96-startup-pages.git;protocol=https;branch=xlnx_rel_v2021.2 \
	   file://ultra96-startup-page.sh \
	   file://ultra96-startup-page.service \
	   file://ultra96-startup-commands.sh \
	   file://launch-ultra96-startup-page.desktop \
	   file://launch-ultra96-startup-page.sh \
	   file://connman_settings \
"
inherit update-rc.d systemd

DEPENDS += "rsync-native"
#RDEPENDS_${PN} = "ace-cloud-editor chromium-x11 python3-itsdangerous python3-markupsafe python3-jinja2 python3-werkzeug python3-flask bash connman connman-client connman-tools"
RDEPENDS_${PN}="bash"

INITSCRIPT_NAME = "ultra96-startup-page.sh"
INITSCRIPT_PARAMS = "start 99 S ."

SYSTEMD_PACKAGES="${PN}"
SYSTEMD_SERVICE_${PN}="ultra96-startup-page.service"
SYSTEMD_AUTO_ENABLE_${PN}="enable"

PV = "1.0+git${SRCPV}"
SRCREV = "276b6efd462fc14f22dcea1af4c51cc3d31d1c95"

FILES_${PN} += "${datadir}/ultra96-startup-pages"
FILES_${PN} += "${base_sbindir}/ /var/lib/connman/"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_ultra96 = "${MACHINE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

do_install () {
    install -d ${D}${datadir}/ultra96-startup-pages
    rsync -r --exclude=".*" ${S}/* ${D}${datadir}/ultra96-startup-pages

    install -d ${D}${base_sbindir}/
    install -m 0755 ${WORKDIR}/launch-ultra96-startup-page.sh ${D}${base_sbindir}/launch-ultra96-startup-page.sh
    install -m 0755 ${WORKDIR}/launch-ultra96-startup-page.desktop ${D}${base_sbindir}/launch-ultra96-startup-page.desktop

    install -d ${D}/var/lib/connman
    install -m 0755 ${WORKDIR}/connman_settings ${D}/var/lib/connman/settings

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/ultra96-startup-commands.sh ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/ultra96-startup-page.service ${D}${systemd_system_unitdir}

    if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
    	install -d ${D}${sysconfdir}/init.d/
    	install -m 0755 ${WORKDIR}/ultra96-startup-page.sh ${D}${sysconfdir}/init.d/ultra96-startup-page.sh
    fi

}

PACKAGE_ARCH_ultra96 = "${BOARD_ARCH}"
