/*     */ package org.firstinspires.ftc.robotcore.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Dom2XmlPullBuilder
/*     */ {
/*     */   protected static final boolean NAMESPACES_SUPPORTED = false;
/*     */   
/*     */   protected Document newDoc()
/*     */     throws XmlPullParserException
/*     */   {
/*     */     try
/*     */     {
/*  79 */       DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
/*  80 */       DocumentBuilder builder = domFactory.newDocumentBuilder();
/*  81 */       DOMImplementation impl = builder.getDOMImplementation();
/*  82 */       return builder.newDocument();
/*     */     } catch (FactoryConfigurationError ex) {
/*  84 */       throw new XmlPullParserException("could not configure factory JAXP DocumentBuilderFactory: " + ex, null, ex);
/*     */     }
/*     */     catch (ParserConfigurationException ex) {
/*  87 */       throw new XmlPullParserException("could not configure parser JAXP DocumentBuilderFactory: " + ex, null, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected XmlPullParser newParser() throws XmlPullParserException
/*     */   {
/*  93 */     XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
/*  94 */     return factory.newPullParser();
/*     */   }
/*     */   
/*     */   public Element parse(Reader reader) throws XmlPullParserException, IOException {
/*  98 */     Document docFactory = newDoc();
/*  99 */     return parse(reader, docFactory);
/*     */   }
/*     */   
/*     */   public Element parse(Reader reader, Document docFactory)
/*     */     throws XmlPullParserException, IOException
/*     */   {
/* 105 */     XmlPullParser pp = newParser();
/* 106 */     pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
/* 107 */     pp.setInput(reader);
/* 108 */     pp.next();
/* 109 */     return parse(pp, docFactory);
/*     */   }
/*     */   
/*     */   public Element parse(XmlPullParser pp, Document docFactory)
/*     */     throws XmlPullParserException, IOException
/*     */   {
/* 115 */     Element root = parseSubTree(pp, docFactory);
/* 116 */     return root;
/*     */   }
/*     */   
/*     */   public Element parseSubTree(XmlPullParser pp) throws XmlPullParserException, IOException {
/* 120 */     Document doc = newDoc();
/* 121 */     Element root = parseSubTree(pp, doc);
/* 122 */     return root;
/*     */   }
/*     */   
/*     */   public Element parseSubTree(XmlPullParser pp, Document docFactory)
/*     */     throws XmlPullParserException, IOException
/*     */   {
/* 128 */     BuildProcess process = new BuildProcess(null);
/* 129 */     return process.parseSubTree(pp, docFactory);
/*     */   }
/*     */   
/*     */   static class BuildProcess {
/*     */     private XmlPullParser pp;
/*     */     private Document docFactory;
/* 135 */     private boolean scanNamespaces = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Element parseSubTree(XmlPullParser pp, Document docFactory)
/*     */       throws XmlPullParserException, IOException
/*     */     {
/* 143 */       this.pp = pp;
/* 144 */       this.docFactory = docFactory;
/* 145 */       return parseSubTree();
/*     */     }
/*     */     
/*     */ 
/*     */     private Element parseSubTree()
/*     */       throws XmlPullParserException, IOException
/*     */     {
/* 152 */       if (this.pp.getEventType() == 0) {
/* 153 */         while (this.pp.getEventType() != 2) {
/* 154 */           this.pp.next();
/*     */         }
/*     */       }
/*     */       
/* 158 */       this.pp.require(2, null, null);
/* 159 */       String name = this.pp.getName();
/* 160 */       String ns = this.pp.getNamespace();
/*     */       
/* 162 */       String prefix = null;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */       String qname = prefix != null ? prefix + ":" + name : name;
/* 174 */       Element parent = this.docFactory.createElementNS(ns, qname);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 182 */       for (int i = 0; i < this.pp.getAttributeCount(); i++)
/*     */       {
/* 184 */         String attrNs = this.pp.getAttributeNamespace(i);
/* 185 */         String attrName = this.pp.getAttributeName(i);
/* 186 */         String attrValue = this.pp.getAttributeValue(i);
/* 187 */         if ((attrNs == null) || (attrNs.length() == 0)) {
/* 188 */           parent.setAttribute(attrName, attrValue);
/*     */         } else {
/* 190 */           String attrPrefix = this.pp.getAttributePrefix(i);
/* 191 */           String attrQname = attrPrefix != null ? attrPrefix + ":" + attrName : attrName;
/* 192 */           parent.setAttributeNS(attrNs, attrQname, attrValue);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 197 */       while (this.pp.next() != 3) {
/* 198 */         if (this.pp.getEventType() == 2) {
/* 199 */           Element el = parseSubTree(this.pp, this.docFactory);
/* 200 */           parent.appendChild(el);
/* 201 */         } else if (this.pp.getEventType() == 4) {
/* 202 */           String text = this.pp.getText();
/* 203 */           Text textEl = this.docFactory.createTextNode(text);
/* 204 */           parent.appendChild(textEl);
/*     */         } else {
/* 206 */           throw new XmlPullParserException("unexpected event " + XmlPullParser.TYPES[this.pp.getEventType()], this.pp, null);
/*     */         }
/*     */       }
/*     */       
/* 210 */       this.pp.require(3, ns, name);
/* 211 */       return parent;
/*     */     }
/*     */     
/*     */     private void declareNamespaces(XmlPullParser pp, Element parent)
/*     */       throws DOMException, XmlPullParserException
/*     */     {
/* 217 */       if (this.scanNamespaces) {
/* 218 */         this.scanNamespaces = false;
/* 219 */         int top = pp.getNamespaceCount(pp.getDepth()) - 1;
/*     */         
/*     */         label116:
/* 222 */         for (int i = top; i >= pp.getNamespaceCount(0); i--)
/*     */         {
/*     */ 
/* 225 */           String prefix = pp.getNamespacePrefix(i);
/* 226 */           for (int j = top; j > i; j--)
/*     */           {
/* 228 */             String prefixJ = pp.getNamespacePrefix(j);
/* 229 */             if (((prefix != null) && (prefix.equals(prefixJ))) || ((prefix != null) && (prefix == prefixJ))) {
/*     */               break label116;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 236 */           declareOneNamespace(pp, i, parent);
/*     */         }
/*     */       } else {
/* 239 */         for (int i = pp.getNamespaceCount(pp.getDepth() - 1); 
/* 240 */             i < pp.getNamespaceCount(pp.getDepth()); 
/* 241 */             i++)
/*     */         {
/* 243 */           declareOneNamespace(pp, i, parent);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void declareOneNamespace(XmlPullParser pp, int i, Element parent) throws DOMException, XmlPullParserException
/*     */     {
/* 250 */       String xmlnsPrefix = pp.getNamespacePrefix(i);
/* 251 */       String xmlnsUri = pp.getNamespaceUri(i);
/* 252 */       String xmlnsDecl = xmlnsPrefix != null ? "xmlns:" + xmlnsPrefix : "xmlns";
/* 253 */       parent.setAttributeNS("http://www.w3.org/2000/xmlns/", xmlnsDecl, xmlnsUri);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\exploravision\Desktop\RobotCore-release.jar!\classes.jar!\org\firstinspires\ftc\robotcore\internal\Dom2XmlPullBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */