package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;
import com.abocode.jfaster.core.common.model.common.UploadFile;
import com.abocode.jfaster.core.common.model.json.ImportFile;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.common.util.FileUtils;
import com.abocode.jfaster.core.extend.template.DataSourceMap;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.web.system.application.dto.view.ReflectHelper;
import com.abocode.jfaster.web.system.domain.entity.Operation;
import com.abocode.jfaster.web.system.domain.entity.RoleFunction;
import com.abocode.jfaster.web.system.domain.repository.ResourceService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.domain.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.core.extend.template.Template;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.web.system.domain.entity.Depart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

@Service("resourceService")
@Transactional
public  class ResourceServiceImpl extends CommonRepositoryImpl implements ResourceService {
	/**
	 * 文件上传
	 * 
	 * @param uploadFile
	 * @throws Exception
	 */
	public Object uploadFile(UploadFile uploadFile) {
		Object object = uploadFile.getObject();
		if(uploadFile.getFileKey()!=null)
		{
			commonDao.update(object);
		}
		else {
		try {
			uploadFile.getMultipartRequest().setCharacterEncoding("UTF-8");
			MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
			ReflectHelper reflectHelper = new ReflectHelper(uploadFile.getObject());
			String  localDiskPath= ResourceUtils.getResourceLocalPath();


			String path="";
			String realPath="";
			String entityName = uploadFile.getObject().getClass().getSimpleName();
			// 设置文件上传路径
			if (entityName.equals("TSTemplate")) {
				realPath =localDiskPath +"/"+ResourceUtils.RESOURCE_TEMPLATE + "/";
				path =ResourceUtils.RESOURCE_TEMPLATE + "/";
			} else if (entityName.equals("TSIcon")) {
				realPath =localDiskPath+"/"+ uploadFile.getCusPath() + "/";
				path =uploadFile.getCusPath() + "/";
			}else{
				path = ResourceUtils.RESOURCE_FILE+"/"+uploadFile.getCusPath()+"/"+ResourceUtils.getDateDir()+"/";
				// 文件数据库保存路径
				 realPath =localDiskPath+ "/" + path+"/";// 文件的硬盘真实路径
			}

			File file = new File(realPath);
			if (!file.exists()) {
				file.mkdirs();// 创建根目录
			}
			file = new File(realPath);
			if (!file.exists()) {
				file.mkdirs();// 创建文件自定义子目录
			}
			else {
				file = new File(realPath);
				if (!file.exists()) {
					file.mkdir();// 创建文件时间子目录
				}
			}

			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile mf = entity.getValue();// 获取上传文件对象
				String originalFilename = mf.getOriginalFilename();// 获取文件名
				String swfName = PinyinUtils.getPinYinHeadChar(ConvertUtils.replaceBlank(FileUtils.getFilePrefix(originalFilename)));// 取文件名首字母作为SWF文件名
				String extend = FileUtils.getExtend(originalFilename);// 获取文件扩展名
				String fileName =originalFilename;
				if(uploadFile.isRename())
				{
					fileName=ResourceUtils.getFileName(originalFilename);
				}
				String savePath = realPath + fileName;// 文件保存全路径
				String fileprefixName = FileUtils.getFilePrefix(originalFilename);
				if (uploadFile.getTitleField() != null) {
					reflectHelper.setMethodValue(uploadFile.getTitleField(), fileprefixName);// 动态调用set方法给文件对象标题赋值
				}
				if (uploadFile.getExtend() != null) {
					// 动态调用 set方法给文件对象内容赋值
					reflectHelper.setMethodValue(uploadFile.getExtend(), extend);
				}
				if (uploadFile.getByteField() != null) {
					// 二进制文件保存在数据库中
					reflectHelper.setMethodValue(uploadFile.getByteField(), StreamUtils.InputStreamTOByte(mf.getInputStream()));
				}
				File savefile = new File(savePath);
				if (uploadFile.getRealPath() != null) {
					// 设置文件数据库的物理路径
					reflectHelper.setMethodValue(uploadFile.getRealPath(), path + fileName);
				}
				this.saveOrUpdate(object);
				// 文件拷贝到指定硬盘目录
				FileCopyUtils.copy(mf.getBytes(), savefile);


//				if (uploadFile.getSwfpath() != null) {
//					// 转SWF
//					reflectHelper.setMethodValue(uploadFile.getSwfpath(), path + swfName + ".swf");
//					SwfToolsUtil.convert2SWF(savePath);
//				}
//				FileCopyUtils.copy(mf.getBytes(), savefile);

				//注释保存swf文件
//				if (uploadFile.getSwfpath() != null) {
//					// 转SWF
//					reflectHelper.setMethodValue(uploadFile.getSwfpath(), path + FileUtils.getFilePrefix(myfilename) + ".swf");
//					SwfToolsUtil.convert2SWF(savePath);
//				}

			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
		}
		return object;
	}

	/**
	 * 文件下载或预览
	 * 
	 * @param uploadFile
	 * @throws Exception
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile) {
		uploadFile.getResponse().setContentType("UTF-8");
		uploadFile.getResponse().setCharacterEncoding("UTF-8");
		InputStream bis = null;
		BufferedOutputStream bos = null;
		HttpServletResponse response = uploadFile.getResponse();
		HttpServletRequest request = uploadFile.getRequest();
		String ctxPath = request.getSession().getServletContext().getRealPath("/");
		String downLoadPath = "";
		long fileLength = 0;
		try {
		if (uploadFile.getRealPath() != null&&uploadFile.getContent() == null) {
			downLoadPath = ctxPath + uploadFile.getRealPath();
			fileLength = new File(downLoadPath).length();
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		} else {
			if (uploadFile.getContent() != null)
				bis = new ByteArrayInputStream(uploadFile.getContent());
			fileLength = uploadFile.getContent().length;
		}
		if (!uploadFile.isView() && uploadFile.getExtend() != null) {
				if (uploadFile.getExtend().equals("text")) {
					response.setContentType("text/plain;");
				} else if (uploadFile.getExtend().equals("doc")) {
					response.setContentType("application/msword;");
				} else if (uploadFile.getExtend().equals("xls")) {
					response.setContentType("application/ms-excel;");
				} else if (uploadFile.getExtend().equals("pdf")) {
					response.setContentType("application/pdf;");
				} else if (uploadFile.getExtend().equals("jpg") || uploadFile.getExtend().equals("jpeg")) {
					response.setContentType("image/jpeg;");
				} else {
					response.setContentType("application/x-msdownload;");
				}
				response.setHeader("Content-disposition", "attachment; filename=" + new String((uploadFile.getTitleField() + "." + uploadFile.getExtend()).getBytes("GBK"), "ISO8859-1"));
				response.setHeader("Content-Length", String.valueOf(fileLength));
			}
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			LogUtils.error(e.getMessage());
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				LogUtils.error(e.getMessage());
			}
		}
		return response;
	}

	public Map<Object, Object> getDataSourceMap(Template template) {
		return DataSourceMap.getDataSourceMap();
	}

	/**
	 * 生成XML importFile 导出xml工具类
	 */
	public HttpServletResponse createXml(ImportFile importFile) {
		HttpServletResponse response = importFile.getResponse();
		HttpServletRequest request = importFile.getRequest();
		try {
			// 创建document对象
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("UTF-8");
			// 创建根节点
			String rootname = importFile.getEntityName() + "s";
			Element rElement = document.addElement(rootname);
			Class entityClass = importFile.getEntityClass();
			String[] fields = importFile.getField().split(",");
			// 得到导出对象的集合
			List objList = findAll(entityClass);
			Class classType = entityClass.getClass();
			for (Object t : objList) {
				Element childElement = rElement.addElement(importFile.getEntityName());
				for (int i = 0; i < fields.length; i++) {
					String fieldName = fields[i];
					// 第一为实体的主键
					if (i == 0) {
						childElement.addAttribute(fieldName, String.valueOf(TagUtil.fieldNameToValues(fieldName, t)));
					} else {
						Element name = childElement.addElement(fieldName);
						name.setText(String.valueOf(TagUtil.fieldNameToValues(fieldName, t)));
					}
				}

			}
			String ctxPath = request.getSession().getServletContext().getRealPath("");
			File fileWriter = new File(ctxPath + "/" + importFile.getFileName());
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(fileWriter));

			xmlWriter.write(document);
			xmlWriter.close();
			// 下载生成的XML文件
			UploadFile uploadFile = new UploadFile(request, response);
			uploadFile.setRealPath(importFile.getFileName());
			uploadFile.setTitleField(importFile.getFileName());
			uploadFile.setExtend("bak");
			viewOrDownloadFile(uploadFile);
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
		return response;
	}

	/**
	 * 解析XML文件将数据导入数据库中
	 */
	@SuppressWarnings("unchecked")
	public void parserXml(String fileName) {
		try {
			File inputXml = new File(fileName);
			Class entityClass;
			// 读取文件
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(inputXml);
			Element employees = document.getRootElement();
			// 遍历根节点下的子节点
			for (Iterator i = employees.elementIterator(); i.hasNext();) {
				Element employee = (Element) i.next();
				// 有实体名反射得到实体类
				entityClass = GenericsUtils.getEntityClass(employee.getName());
				// 得到实体属性
				Field[] fields = TagUtil.getFiled(entityClass);
				// 得到实体的ID
				String id = employee.attributeValue(fields[0].getName());
				// 判断实体是否已存在
				Object obj1 =this.commonDao.findEntity(entityClass, id);
				// 实体不存在new个实体
				if (obj1 == null) {
					obj1 = entityClass.newInstance();
				}
				// 根据反射给实体属性赋值
				for (Iterator j = employee.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					for (int k = 0; k < fields.length; k++) {
						if (node.getName().equals(fields[k].getName())) {
							String fieldName = fields[k].getName();
							String stringLetter = fieldName.substring(0, 1).toUpperCase();
							String setName = "set" + stringLetter + fieldName.substring(1);
							Method setMethod = entityClass.getMethod(setName, new Class[] { fields[k].getType() });
							String type = TagUtil.getColumnType(fieldName, fields);
							if (type.equals("int")) {
								setMethod.invoke(obj1, new Integer(node.getText()));
							} else if (type.equals("string")) {
								setMethod.invoke(obj1, node.getText().toString());
							} else if (type.equals("short")) {
								setMethod.invoke(obj1, new Short(node.getText()));
							} else if (type.equals("double")) {
								setMethod.invoke(obj1, new Double(node.getText()));
							} else if (type.equals("Timestamp")) {
								setMethod.invoke(obj1, new Timestamp(DateUtils.strToDate(node.getText(), DateUtils.YYYY_MM_DD_HH_MM_SS).getTime()));
							}
						}
					}
				}
				if (obj1 != null) {
					saveOrUpdate(obj1);
				} else {
					save(obj1);
				}
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
		}
	}

	/**
	 * 根据模型生成JSOn 全部对象
	 * @param depart
	 *            已拥有的对象
	 * @param recursive
	 *            模型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ComboTree tree(Depart depart, boolean recursive) {
		ComboTree tree = new ComboTree();
		tree.setId(ConvertUtils.getString(depart.getId()));
		tree.setText(depart.getDepartname());
		List<Depart> departsList = this.commonDao.findAllByProperty(Depart.class, "TSPDepart.id", depart.getId());
		if (departsList != null && departsList.size() > 0) {
			tree.setState("closed");
			tree.setChecked(false);
			if (recursive) {// 递归查询子节点
				List<Depart> departList = new ArrayList<Depart>(departsList);
				//Collections.sort(departList, new SetListSort());// 排序
				List<ComboTree> children = new ArrayList<ComboTree>();
				for (Depart d : departList) {
					ComboTree t = tree(d, true);
					children.add(t);
				}
				tree.setChildren(children);
			}
		}
		return tree;
	}

	public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel, List in, boolean recursive) {
		List<ComboTree> trees = new ArrayList<ComboTree>();
		for (Object obj : all) {
			trees.add(comboTree(obj, comboTreeModel, in, recursive));
		}
		return trees;

	}

   /**
    * 构建ComboTree
    * @param obj
    * @param comboTreeModel
    * @param in
    * @param recursive 是否递归子节点
    * @return
    */
	private ComboTree comboTree(Object obj, ComboTreeModel comboTreeModel, List in, boolean recursive) {
		ComboTree tree = new ComboTree();
		Map<String, Object> attributes = new HashMap<String, Object>();
		ReflectHelper reflectHelper = new ReflectHelper(obj);
		String id = ConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getIdField()));
		tree.setId(id);
		tree.setText(ConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getTextField())));
		if (comboTreeModel.getSrcField() != null) {
			attributes.put("href", ConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getSrcField())));
			tree.setAttributes(attributes);
		}
		if (in == null) {
		} else {
			if (in.size() > 0) {
				for (Object inobj : in) {
					ReflectHelper reflectHelper2 = new ReflectHelper(inobj);
					String inId = ConvertUtils.getString(reflectHelper2.getMethodValue(comboTreeModel.getIdField()));
                   if (inId.equals(id)) {
						tree.setChecked(true);
					}
				}
			}
		}

		List curChildList = (List) reflectHelper.getMethodValue(comboTreeModel.getChildField());
		if (curChildList != null && curChildList.size() > 0) {
			tree.setState("closed");
			tree.setChecked(false);

           if (recursive) { // 递归查询子节点
               List<ComboTree> children = new ArrayList<ComboTree>();
               List nextChildList = new ArrayList(curChildList);
               for (Object childObj : nextChildList) {
                   ComboTree t = comboTree(childObj, comboTreeModel, in, recursive);
                   children.add(t);
               }
               tree.setChildren(children);
           }
       }
		return tree;
	}
	/**
	 * 构建树形数据表
	 */
	public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel) {
		List<TreeGrid> treegrid = new ArrayList<TreeGrid>();
		for (Object obj : all) {
			ReflectHelper reflectHelper = new ReflectHelper(obj);
			TreeGrid tg = new TreeGrid();
			String id = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getIdField()));
			String src = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getSrc()));
			String text = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getTextField()));
			if(!StringUtils.isEmpty(treeGridModel.getOrder())){
				String order = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getOrder()));
				tg.setOrder(order);
			}
			tg.setId(id);
			if (treeGridModel.getIcon() != null) {
				String iconpath = TagUtil.fieldNameToValues(treeGridModel.getIcon(), obj).toString();
				if (iconpath != null) {
					tg.setCode(iconpath);
				} else {
					tg.setCode("");
				}
			}
			tg.setSrc(src);
			tg.setText(text);
			if (treeGridModel.getParentId() != null) {
				Object pid = TagUtil.fieldNameToValues(treeGridModel.getParentId(), obj);
				if (pid != null) {
					tg.setParentId(pid.toString());
				} else {
					tg.setParentId("");
				}
			}
			if (treeGridModel.getParentText() != null) {
				Object ptext = TagUtil.fieldNameToValues(treeGridModel.getTextField(), obj);
				if (ptext != null) {
					tg.setParentText(ptext.toString());
				} else {
					tg.setParentText("");
				}

			}
			List childList = (List) reflectHelper.getMethodValue(treeGridModel.getChildList());

			if (childList != null && childList.size() > 0) {
				tg.setState("closed");
			}
			if (treeGridModel.getRoleid() != null) {
				String[] opStrings = {};
				List<RoleFunction> roleFunctions =this.commonDao.findAllByProperty(RoleFunction.class, "TSFunction.id", id);

				if (roleFunctions.size() > 0) {
					for (RoleFunction tRoleFunction : roleFunctions) {
						RoleFunction roleFunction = tRoleFunction;
						if (roleFunction.getTSRole().getId().toString().equals(treeGridModel.getRoleid())) {
							String bbString = roleFunction.getOperation();
							if (bbString != null) {
								opStrings = bbString.split(",");
								break;
							}
						}
					}
				}
				List<Operation> operateions =this.commonDao.findAllByProperty(Operation.class, "TSFunction.id", id);
				StringBuffer attributes = new StringBuffer();
				if (operateions.size() > 0) {
					for (Operation tOperation : operateions) {
						if (opStrings.length < 1) {
							attributes.append("<input type=checkbox name=operatons value=" + tOperation.getId() + "_" + id + ">" + tOperation.getOperationname());
						} else {
							StringBuffer sb = new StringBuffer();
							sb.append("<input type=checkbox name=operatons");
							for (int i = 0; i < opStrings.length; i++) {
								if (opStrings[i].equals(tOperation.getId().toString())) {
									sb.append(" checked=checked");
								}
							}
							sb.append(" value=" + tOperation.getId() + "_" + id + ">" + tOperation.getOperationname());
							attributes.append(sb.toString());
						}
					}
				}
				tg.setOperations(attributes.toString());
			}
           if (treeGridModel.getFieldMap() != null) {
               tg.setFieldMap(new HashMap<String, Object>());
               for (Map.Entry<String, Object> entry : treeGridModel.getFieldMap().entrySet()) {
                   Object fieldValue = reflectHelper.getMethodValue(entry.getValue().toString());
                   tg.getFieldMap().put(entry.getKey(), fieldValue);
               }
           }
           if (treeGridModel.getFunctionType() != null) {
           	String functionType = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getFunctionType()));
           	tg.setFunctionType(functionType);
           }
			treegrid.add(tg);
		}
		return treegrid;
	}

	

	@Override
	public List<ComboTree> comTree(List<Depart> all, ComboTree comboTree) {
	List<ComboTree> trees = new ArrayList<ComboTree>();
	for (Depart depart : all) {
		trees.add(tree(depart, true));
	}
	return trees;

	}
   
	@Override
	public String getUploadFileContent(UploadFile uploadFile) {
		StringBuffer content=new StringBuffer();
		try {
			uploadFile.getMultipartRequest().setCharacterEncoding("UTF-8");
			MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			if(fileMap.size()>1){
				 throw new Exception("只支持单个文件上传");
			}
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile mf = entity.getValue();// 获取上传文件对象
				
				InputStream stream = mf.getInputStream();
				  BufferedReader bufferedreader = null;
				  try{
			            bufferedreader = new BufferedReader(new InputStreamReader(stream, "gb2312"));
			            String s = null;
			            while(null != (s = bufferedreader.readLine())){
			            	content.append(s).append(",");
			            }
			        }catch(Exception e) {
			            LogUtils.error(e.getMessage());
			        }

			}
		} catch (Exception e1) {
			return "";
		}
		return content.toString();
	}	
	
	
	
	
}
