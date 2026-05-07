<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
    
    <xsl:template match="/">
        <html>
            <head>
                <title>EFEW32 Órarend</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 20px;
                        background-color: #f5f5f5;
                    }
                    h1 {
                        color: #333;
                        text-align: center;
                        border-bottom: 2px solid #333;
                        padding-bottom: 10px;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin: 20px 0;
                        background-color: white;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    }
                    th {
                        background-color: #4CAF50;
                        color: white;
                        padding: 12px;
                        text-align: left;
                        font-weight: bold;
                    }
                    td {
                        padding: 10px;
                        border: 1px solid #ddd;
                    }
                    tr:nth-child(even) {
                        background-color: #f9f9f9;
                    }
                    tr:hover {
                        background-color: #f5f5f5;
                    }
                    .eloadas {
                        background-color: #e8f5e8;
                    }
                    .gyakorlat {
                        background-color: #fff3cd;
                    }
                    .info {
                        margin: 10px 0;
                        padding: 10px;
                        background-color: #e7f3ff;
                        border-left: 4px solid #2196F3;
                    }
                </style>
            </head>
            <body>
                <h1>EFEW32 - Programtervező Informatikus Órarend</h1>
                
                <div class="info">
                    <strong>Szak:</strong> Programtervező Informatikus<br/>
                    <strong>Összes óra:</strong> <xsl:value-of select="count(//ora)"/>
                </div>
                
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Típus</th>
                            <th>Kurzus</th>
                            <th>Nap</th>
                            <th>Időpont</th>
                            <th>Helyszín</th>
                            <th>Oktató</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="//ora">
                            <tr>
                                <xsl:attribute name="class">
                                    <xsl:value-of select="@tipus"/>
                                </xsl:attribute>
                                <td><xsl:value-of select="@id"/></td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="@tipus='eloadas'">Előadás</xsl:when>
                                        <xsl:when test="@tipus='gyakorlat'">Gyakorlat</xsl:when>
                                        <xsl:otherwise><xsl:value-of select="@tipus"/></xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td><xsl:value-of select="kurzus"/></td>
                                <td><xsl:value-of select="idopont/nap"/></td>
                                <td>
                                    <xsl:value-of select="idopont/tol"/> - <xsl:value-of select="idopont/ig"/>
                                </td>
                                <td><xsl:value-of select="helyszin"/></td>
                                <td><xsl:value-of select="oktato"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
                
                <h2>Napok szerinti bontás</h2>
                <xsl:for-each select="//idopont/nap[not(.=preceding::idopont/nap)]">
                    <xsl:sort select="." order="ascending"/>
                    <xsl:variable name="current_nap" select="."/>
                    <h3><xsl:value-of select="$current_nap"/></h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Idő</th>
                                <th>Kurzus</th>
                                <th>Típus</th>
                                <th>Helyszín</th>
                                <th>Oktató</th>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:for-each select="//ora[idopont/nap=$current_nap]">
                                <xsl:sort select="idopont/tol"/>
                                <tr>
                                    <xsl:attribute name="class">
                                        <xsl:value-of select="@tipus"/>
                                    </xsl:attribute>
                                    <td><xsl:value-of select="idopont/tol"/> - <xsl:value-of select="idopont/ig"/></td>
                                    <td><xsl:value-of select="kurzus"/></td>
                                    <td>
                                        <xsl:choose>
                                            <xsl:when test="@tipus='eloadas'">Előadás</xsl:when>
                                            <xsl:when test="@tipus='gyakorlat'">Gyakorlat</xsl:when>
                                            <xsl:otherwise><xsl:value-of select="@tipus"/></xsl:otherwise>
                                        </xsl:choose>
                                    </td>
                                    <td><xsl:value-of select="helyszin"/></td>
                                    <td><xsl:value-of select="oktato"/></td>
                                </tr>
                            </xsl:for-each>
                        </tbody>
                    </table>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>