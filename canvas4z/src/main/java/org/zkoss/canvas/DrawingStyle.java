/* DrawingStyle.java

{{IS_NOTE
 Purpose:
  
 Description:
  
 History:
  Jul 2, 2011 9:48:31 PM , Created by simonpai
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.canvas;

import java.awt.geom.Path2D;
import java.util.Iterator;

import org.zkoss.canvas.drawable.Path;
import org.zkoss.canvas.drawable.Shape;
import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

/**
 * 
 * @author simonpai
 */
public class DrawingStyle implements JSONAware {
	
	private Attrs _attrs;
	private java.awt.Shape _clipping; // cached clipping
	
	/**
	 * Create an empty DrawingStyle
	 */
	public DrawingStyle() {
		_attrs = new Attrs();
	}
	
	/**
	 * Create a cloned DrawingStyle from another.
	 */
	public DrawingStyle(DrawingStyle style) {
		_attrs = new Attrs(style._attrs);
	}
	
	// state management //
	/**
	 * Removes all drawing styles.
	 */
	public DrawingStyle clear() {
		for(AttrDef d : AttrDef.values()) 
			remove(d);
		return this;
	}
	
	
	
	// Drawing Type
	/*
	 * NOT defined in DOM Spec but introduced for better concept organization.
	 * See Canvas.js #_paintRect, #_paintPath, #_paintText
	 */
	public static class DrawingType {
		public static final String NONE = "none";
		public static final String FILL = "fill";
		public static final String STROKE = "stroke";
		public static final String BOTH = "both";
		
		public static final String DEFAULT = FILL;
	}
	
	/**
	 * Returns the drawing type.
	 */
	public String getDrawingType(){
		return (String) get(AttrDef.DRAWING_TYPE);
	}
	
	/**
	 * Sets the drawing type. 
	 * @param type: Default value is FILL.
	 */
	public DrawingStyle setDrawingType(String value){
		set(AttrDef.DRAWING_TYPE, value);
		return this;
	}
	
	/**
	 * Removes the drawing type setting.
	 */
	public DrawingStyle removeDrawingType(){
		remove(AttrDef.DRAWING_TYPE);
		return this;
	}
	
	
	
	// Transformation
	public static class Transformation {
		private static final double[] DEFAULT = {1,0,0,1,0,0};
	}
	
	/**
	 * Returns the transformation matrix entries
	 * @return A double array of length 6, with entries representing m11, m12
	 * m21, m22, dx, dy respectively 
	 */
	public double[] getTransformation(){
		return (double[]) get(AttrDef.TRANSFORMATION);
	}
	
	/**
	 * Sets the transformation matrix. Note that the default value of m11 and 
	 * m22 are 1, while the rests are 0.
	 */
	public DrawingStyle setTransformation(double m11, double m12, double m21, 
			double m22, double dx, double dy) {
		return setTransformation(new double[] {m11, m12, m21, m22, dx, dy});
	}
	
	/**
	 * Sets transformation matrix by a double Array.
	 * @param value: must be a double Array of length 6, with entries
	 * representing m11, m12, m21, m22, dx, dy respectively. 
	 */
	public DrawingStyle setTransformation(double[] value){
		boolean isDefault = true;
		for(int i = 0; i < 6; i++)
			if(value[i] != Transformation.DEFAULT[i]) 
				isDefault = false;
		
		if(isDefault)
			remove(AttrDef.TRANSFORMATION);
		else
			set(AttrDef.TRANSFORMATION, value);
		return this;
	}
	
	/**
	 * Removes the transformation setting.
	 */
	public DrawingStyle removeTransformation(){
		remove(AttrDef.TRANSFORMATION);
		return this;
	}
	
	
	
	// Clipping
	/**
	 * Returns the clipping region.
	 */
	public java.awt.Shape getClipping(){
		return _clipping;
	}
	
	/**
	 * Sets the clipping region.
	 */
	public DrawingStyle setClipping(Shape shape) {
		_clipping = shape;
		set(AttrDef.CLIPPING, shape);
		return this;
	}
	
	/**
	 * Sets the clipping region. 
	 * @param clipping: Can be any java.awt.Shape
	 */
	public DrawingStyle setClipping(java.awt.Shape shape) {
		// Java 2D uses Bezier curve to simulate arc when passing into Path2D
		// so passing an Arc here will turn it into a path formed by Bezier curves
		_clipping = shape;
		set(AttrDef.CLIPPING, new Path(new Path2D.Double(shape)));
		return this;
	}
	
	/**
	 * Removes the clipping region setting.
	 */
	public DrawingStyle removeClipping(){
		_clipping = null;
		remove(AttrDef.CLIPPING);
		return this;
	}
	
	
	
	// Stroke Style
	/**
	 * Returns stroke style.
	 * @return
	 */
	public Object getStrokeStyle(){
		return get(AttrDef.STROKE_STYLE);
	}
	
	/**
	 * Sets strokeStyle as a CSS color.
	 * @param cssColor: must be a valid CSS color string.
	 */
	public DrawingStyle setStrokeStyle(String cssColor){
		set(AttrDef.STROKE_STYLE, cssColor);
		return this;
	}
	
	// TODO: support gradient and pattern
	//public void setStrokeStyle(GradientPaint gradient){
	//}
	
	/**
	 * Removes strokeStyle setting.
	 * @return
	 */
	public DrawingStyle removeStrokeStyle(){
		remove(AttrDef.STROKE_STYLE);
		return this;
	}
	
	
	
	// Fill Style
	/**
	 * Returns fill style.
	 * @return
	 */
	public Object getFillStyle(){
		return get(AttrDef.FILL_STYLE);
	}
	
	/**
	 * Sets fillStyle as a CSS color.
	 * @param cssColor: must be a valid CSS color string.
	 */
	public DrawingStyle setFillStyle(String cssColor){
		set(AttrDef.FILL_STYLE, cssColor);
		return this;
	}
	
	// TODO: support gradient and pattern
	//public void setFillStyle(GradientPaint gradient){
	//}
	
	/**
	 * Removes fillStyle setting.
	 * @return
	 */
	public DrawingStyle removeFillStyle(){
		remove(AttrDef.FILL_STYLE);
		return this;
	}
	
	
	
	// Alpha
	/**
	 * Returns the alpha value. 
	 */
	public double getAlpha(){
		return (Double) get(AttrDef.ALPHA);
	}
	
	/**
	 * Sets the alpha value.
	 * @param alpha: must be in range [0,1].
	 * Any value less then 0 or greater than 1 is ignored.
	 */
	public DrawingStyle setAlpha(double value){
		if(value < 0 || value > 1) 
			return this;
		set(AttrDef.ALPHA, value);
		return this;
	}
	
	/**
	 * Removes alpha setting.
	 */
	public DrawingStyle removeAlpha(){
		remove(AttrDef.ALPHA);
		return this;
	}
	
	
	
	// Line Width
	/**
	 * Gets line width setting.
	 */
	public double getLineWidth(){
		return (Double) get(AttrDef.LINE_WIDTH);
	}
	
	/**
	 * Sets line width. Default value is 1.
	 */
	public DrawingStyle setLineWidth(double value){
		set(AttrDef.LINE_WIDTH, value);
		return this;
	}
	
	/**
	 * Removes line width setting
	 */
	public DrawingStyle removeLineWidth(){
		remove(AttrDef.LINE_WIDTH);
		return this;
	}
	
	
	
	// Line Cap
	/*
	 * String values are defined in DOM Canvas spec
	 */
	public static class LineCap {
		public static final String BUTT   = "butt";
		public static final String ROUND  = "round";
		public static final String SQUARE = "square";
		
		public static final String DEFAULT = BUTT;
	}
	
	/**
	 * Gets line cap setting.
	 */
	public String getLineCap(){
		return (String) get(AttrDef.LINE_CAP);
	}
	
	/**
	 * Sets line cap type. Default value is BUTT.
	 */
	public DrawingStyle setLineCap(String value){
		set(AttrDef.LINE_CAP, value);
		return this;
	}
	
	/**
	 * Removes line cap setting.
	 */
	public DrawingStyle removeLineCap(){
		remove(AttrDef.LINE_CAP);
		return this;
	}
	
	
	
	// Line Join
	/*
	 * String values are defined in DOM Canvas spec
	 */
	public static class LineJoin {
		public static final String ROUND = "round";
		public static final String BEVEL = "bevel";
		public static final String MITER = "miter";
		
		public static final String DEFAULT = MITER;
	}
	
	/**
	 * Gets line join setting.
	 */
	public String getLineJoin(){
		return (String) get(AttrDef.LINE_JOIN);
	}
	
	/**
	 * Sets line join type. Default value is MITER 
	 */
	public DrawingStyle setLineJoin(String value){
		set(AttrDef.LINE_JOIN, value);
		return this;
	}
	
	/**
	 * Removes line join setting.
	 */
	public DrawingStyle removeLineJoin(){
		remove(AttrDef.LINE_JOIN);
		return this;
	}
	
	
	
	// Miter Limit
	/**
	 * Get miter limit setting.
	 */
	public double getMiterLimit(){
		return (Double) get(AttrDef.MITER_LIMIT);
	}
	
	/**
	 * Sets miter limit. Default value is 10.
	 */
	public DrawingStyle setMiterLimit(double value){
		set(AttrDef.MITER_LIMIT, value);
		return this;
	}
	
	/**
	 * Removes miter limit setting.
	 */
	public DrawingStyle removeMiterLimit(){
		remove(AttrDef.MITER_LIMIT);
		return this;
	}
	
	
	
	// Shadow Offset
	/**
	 * Returns shadow offset X value.
	 */
	public double getShadowOffsetX(){
		return (Double) get(AttrDef.SHADOW_OFFSET_X);
	}
	
	/**
	 * Returns shadow offset Y value.
	 */
	public double getShadowOffsetY(){
		return (Double) get(AttrDef.SHADOW_OFFSET_Y);
	}
	
	/**
	 * Sets shadow offset.
	 */
	public DrawingStyle setShadowOffset(double x, double y){
		set(AttrDef.SHADOW_OFFSET_X, x);
		set(AttrDef.SHADOW_OFFSET_Y, y);
		return this;
	}
	
	/**
	 * Removes shadow offset setting.
	 */
	public DrawingStyle removeShadowOffset(){
		remove(AttrDef.SHADOW_OFFSET_X);
		remove(AttrDef.SHADOW_OFFSET_Y);
		return this;
	}
	
	
	
	// Shadow Blur
	/**
	 * Returns shadow blur value.
	 */
	public double getShadowBlur(){
		return (Double) get(AttrDef.SHADOW_BLUR);
	}
	
	/**
	 * Sets shadow blur value.
	 */
	public DrawingStyle setShadowBlur(double value){
		set(AttrDef.SHADOW_BLUR, value);
		return this;
	}
	
	/**
	 * Removes shadow blur setting.
	 */
	public DrawingStyle removeShadowBlur(){
		remove(AttrDef.SHADOW_BLUR);
		return this;
	}
	
	
	
	// Shadow Color
	/**
	 * Returns shadow color as a css color string. 
	 */
	public String getShadowColor(){
		return (String) get(AttrDef.SHADOW_COLOR);
	}
	
	/**
	 * Sets shadow color
	 * @param cssColor: must be a valid css color
	 */
	public DrawingStyle setShadowColor(String cssColor){
		set(AttrDef.SHADOW_COLOR, cssColor);
		return this;
	}
	
	/**
	 * Removes shadow color setting.
	 */
	public DrawingStyle removeShadowColor(){
		remove(AttrDef.SHADOW_COLOR);
		return this;
	}
	
	
	
	// Composite
	/* 
	 * These values are exactly the String values defined in DOM Canvas spec,
	 * and will be packed into JSON object as Map values. Some of them are
	 * long Strings, but using it directly saves the work (and trouble) to 
	 * convert it back at client side.
	 */
	public static class Composite {
		public static final String SRC_ATOP  = "source-atop";
		public static final String SRC_IN    = "source-in";
		public static final String SRC_OUT   = "source-out";
		public static final String SRC_OVER  = "source-over";
		public static final String DEST_ATOP = "destination-atop";
		public static final String DEST_IN   = "destination-in";
		public static final String DEST_OUT  = "destination-out";
		public static final String DEST_OVER = "destination-over";
		public static final String LIGHTER   = "lighter";
		public static final String COPY      = "copy";
		public static final String XOR       = "xor";
		
		public static final String DEFAULT = SRC_OVER;
	}
	
	/**
	 * Returns the composite value.
	 */
	public String getComposite(){
		return (String) get(AttrDef.COMPOSITE);
	}
	
	/**
	 * Sets the composite value.
	 * @param composite: must be chosen from the given constant strings in
	 * Drawable.Composite, or a string that specifies a vendor-specific
	 * composite operation. (See HTML Canvas doc)
	 */
	public DrawingStyle setComposite(String value){
		set(AttrDef.COMPOSITE, value);
		return this;
	}
	
	/**
	 * Removes the composite setting.
	 */
	public DrawingStyle removeComposite(){
		remove(AttrDef.COMPOSITE);
		return this;
	}
	
	
	
	// Font
	/**
	 * Returns font setting.
	 */
	public String getFont(){
		return (String) get(AttrDef.FONT);
	}
	
	/**
	 * Sets the font.
	 */
	public DrawingStyle setFont(String value){
		set(AttrDef.FONT, value);
		return this;
	}
	
	/**
	 * Removes font setting.
	 */
	public DrawingStyle removeFont(){
		remove(AttrDef.FONT);
		return this;
	}
	
	
	
	// Text Alignment
	/*
	 * String values are defined in DOM Canvas spec
	 */
	public static class TextAlign {
		public static final String START  = "start";
		public static final String END    = "end";
		public static final String LEFT   = "left";
		public static final String RIGHT  = "right";
		public static final String CENTER = "center";
		
		public static final String DEFAULT = START;
	}
	
	/**
	 * Returns text align setting.
	 */
	public String getTextAlign(){
		return (String) get(AttrDef.TEXT_ALIGN);
	}
	
	/**
	 * Sets text align.
	 * @param value: 
	 */
	public DrawingStyle setTextAlign(String value){
		set(AttrDef.TEXT_ALIGN, value);
		return this;
	}
	
	/**
	 * Removes text align
	 */
	public DrawingStyle removeTextAlign(){
		remove(AttrDef.TEXT_ALIGN);
		return this;
	}
	
	
	
	// Text Baseline
	/*
	 * String values are defined in DOM Canvas spec
	 */
	public static class TextBaseline {
		public static final String TOP         = "top";
		public static final String HANGING     = "hanging";
		public static final String MIDDLE      = "middle";
		public static final String ALPHABETIC  = "alphabetic";
		public static final String IDEOGRAPHIC = "ideographic";
		public static final String BOTTOM      = "bottom";
		
		public static final String DEFAULT = ALPHABETIC;
	}
	
	/**
	 * Returns text baseline setting.
	 */
	public String getTextBaseline(){
		return (String) get(AttrDef.TEXT_BASELINE);
	}
	
	/**
	 * Sets text baseline.
	 */
	public DrawingStyle setTextBaseline(String value){
		set(AttrDef.TEXT_BASELINE, value);
		return this;
	}
	
	/**
	 * Removes text baseline setting
	 */
	public DrawingStyle removeTextBaseline(){
		remove(AttrDef.TEXT_BASELINE);
		return this;
	}
	
	
	
	// Text Max Width
	/*
	 * NOT defined in DOM Canvas spec 
	 */
	/**
	 * Returns max width setting.
	 */
	public double getMaxWidth(){
		return (Double) get(AttrDef.TEXT_MAX_WIDTH);
	}
	
	/**
	 * Sets text max width. Default value is -1.
	 * @param value: Any value less than 0 means no limit on maximal width
	 */
	public DrawingStyle setMaxWidth(double value){
		set(AttrDef.TEXT_MAX_WIDTH, value);
		return this;
	}
	
	/**
	 * Removes text max width limit
	 */
	public DrawingStyle removeMaxWidth(){
		remove(AttrDef.TEXT_MAX_WIDTH);
		return this;
	}
	// end of state management //
	
	
	
	// helper //
	private Object get(AttrDef def){
		return get(def, true);
	}
	
	private Object get(AttrDef def, boolean returnDefaultValue){
		Object result = _attrs.get(def.getKey());
		return (returnDefaultValue && result == null) ? def.getDefault() : result;
	}
	
	@SuppressWarnings("unchecked")
	private void set(AttrDef def, Object value){
		if(value.equals(def.getDefault()))
			_attrs.remove(def.getKey());
		else
			_attrs.put(def.getKey(), value);
	}
	
	private Object remove(AttrDef def){
		return _attrs.remove(def.getKey());
	}
	
	
	
	// An utility class to store data as a Map
	@SuppressWarnings("serial")
	private class Attrs extends JSONObject {
		
		/* copy & merge */
		private Attrs(){}
		
		private Attrs(Attrs attr2){
			copyFrom(attr2);
		}
		
		// copy values from attrs, always overwrites
		@SuppressWarnings("unchecked")
		private void copyFrom(Attrs attr2) {
			Iterator<Entry<String, Object>> itr = attr2.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<String, Object> e = itr.next();
				put(e.getKey(), e.getValue());
			}
		}
	}
	
	/* 
	 * private constants helpers (for component developer)
	 *  
	 * These values are the keys of Drawing State attributes when converted
	 * into a JSON object. It must exactly match the setting in Canvas.js 
	 * #_setDOMContextState
	 */
	private enum AttrDef {
		DRAWING_TYPE    ("dwtp", DrawingType.DEFAULT),
		TRANSFORMATION  ("trns", null), // manually handle default check
		CLIPPING        ("clp",  null),
		STROKE_STYLE    ("strk", "#000000"), 
		FILL_STYLE      ("fil",  "#000000"), 
		ALPHA           ("alfa", 1.0),
		LINE_WIDTH      ("lnw",  1),
		LINE_CAP        ("lncp", LineCap.DEFAULT),
		LINE_JOIN       ("lnj",  LineJoin.DEFAULT),
		MITER_LIMIT     ("mtr",  10),
		SHADOW_OFFSET_X ("shx",  0),
		SHADOW_OFFSET_Y ("shy",  0),
		SHADOW_BLUR     ("shb",  0),
		SHADOW_COLOR    ("shc",  ""), // default transparent black
		COMPOSITE       ("cmp",  Composite.DEFAULT),
		FONT            ("fnt",  "10px sans-serif"),
		TEXT_ALIGN      ("txal", TextAlign.DEFAULT),
		TEXT_BASELINE   ("txbl", TextBaseline.DEFAULT),
		TEXT_MAX_WIDTH  ("txmw", -1);
		
		String _key;
		Object _defaultValue;
		
		private AttrDef(String key, Object defaultValue){
			_key = key;
			_defaultValue = defaultValue;
		}
		
		public String getKey(){
			return _key;
		}
		
		public Object getDefault(){
			return _defaultValue;
		}
	}
	
	@Override
	public String toJSONString() {
		return _attrs.toJSONString();
	}
	
}
