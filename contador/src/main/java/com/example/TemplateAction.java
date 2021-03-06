package com.example;


/*
 * Please change this class's package to your genearated Plug-in's package.
 * Plug-in's package namespace => com.example
 *   com.change_vision.astah.extension.plugin => X
 *   com.example                              => O
 *   com.example.internal                     => O
 *   learning                                 => X
 */

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.IUseCase;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class TemplateAction implements IPluginActionDelegate {
    public Object run(IWindow window) throws UnExpectedException {
        try {
            ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
            IModel iCurrentProject = projectAccessor.getProject();
            List<IUseCase> classeList = new ArrayList<IUseCase>();
            getAllClasses(iCurrentProject, classeList);
            
            int totalWeight = 0;
            
            for (IUseCase caso : classeList) {
            	int tempWeight = 3;
            	for (String stereotype : caso.getStereotypes()) {
            		if (stereotype.toLowerCase().equals("small")){
            			tempWeight = 3;
            			break;
            		} else if (stereotype.toLowerCase().equals("medium")){
            			tempWeight = 5;
            			break;
            		} else if (stereotype.toLowerCase().equals("large")){
            			tempWeight = 7;
            			break;
            		}
            	}
            	totalWeight += tempWeight;
            }
            
            JOptionPane.showMessageDialog(window.getParent(), 
            		"Total weight is " + totalWeight + ".");
        } catch (ProjectNotFoundException e) {
            String message = "Please open a project";
            JOptionPane.showMessageDialog(window.getParent(), message, 
            		"Warning", JOptionPane.WARNING_MESSAGE); 
            throw new CalculateUnExpectedException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window.getParent(), 
            		"Exception occured", "Alert", JOptionPane.ERROR_MESSAGE); 
            throw new UnExpectedException();
        }
        return null;
    }
    
    public class CalculateUnExpectedException extends UnExpectedException {
    } 

    private void getAllClasses(INamedElement element, List<IUseCase> classList) throws ClassNotFoundException, ProjectNotFoundException {
        if (element instanceof IPackage) {
            for(INamedElement ownedNamedElement : ((IPackage)element).getOwnedElements()) {
                getAllClasses(ownedNamedElement, classList);
            }
        } else if (element instanceof IUseCase) {
            classList.add((IUseCase)element);
            for(IClass nestedClasses : ((IUseCase)element).getNestedClasses()) {
                getAllClasses(nestedClasses, classList);
            }
        }
    }
}
